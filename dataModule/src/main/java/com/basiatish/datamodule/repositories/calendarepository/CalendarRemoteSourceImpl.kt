package com.basiatish.datamodule.repositories.calendarepository

import com.basiatish.datamodule.api.BiaApi
import com.basiatish.datamodule.api.entities.DayEntityRemote
import com.basiatish.datamodule.mappers.CalendarMapper
import com.basiatish.domain.common.Result
import com.basiatish.domain.entities.CalendarDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth
import java.time.ZoneId
import java.util.concurrent.TimeUnit


class CalendarRemoteSourceImpl(
    private val service: BiaApi,
    private val mapper: CalendarMapper
) : CalendarRemoteSource {

    private var createdMonth: List<CalendarDay> = listOf()

    override suspend fun getCalendar(date: LocalDate): Result<List<CalendarDay>> =
        withContext(Dispatchers.IO) {
            try {
                val response = service.getCalendar()
                if (response.isSuccessful) {
                    val responseResult = response.body()!!
                    val days = responseResult.days?.values?.toList()
                    return@withContext Result.Success(daysInMonthArray(date, responseResult.dayStart, days))
                } else {
                    return@withContext Result.Error(Exception(response.message()))
                }
            } catch (e: Exception) {
                return@withContext Result.Error(e)
            }
        }

    override suspend fun saveDay(time: Long, isWorkDay: Boolean): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                val selectedTime = Instant.ofEpochMilli(time)
                    .atZone(ZoneId.systemDefault())
                val selectedDay = selectedTime.dayOfMonth
                val selectedMon = YearMonth.from(selectedTime).monthValue
                val selectedYear = Year.from(selectedTime).value
                val response = service.saveDay("${selectedDay}-${selectedMon}-${selectedYear}",
                    mapper.toSaveDayRemote(time, isWorkDay))
                if (response.isSuccessful) {
                    return@withContext Result.Success("Success")
                } else {
                    return@withContext Result.Error(Exception(response.message()))
                }
            } catch (e: Exception) {
                return@withContext Result.Error(e)
            }
        }


    private fun daysInMonthArray(date: LocalDate, startInMills: Long, selectedDays: List<DayEntityRemote>?): List<CalendarDay> {
        val daysInMonthArray = mutableListOf<CalendarDay>()
        val yearMonth = YearMonth.from(date)
        val daysInMonth = yearMonth.lengthOfMonth()
        val firstOfMonth = date.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value
        for (i in 2..43) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add(mapper.toCalendarDay(""))
            } else {
                daysInMonthArray.add(mapper.toCalendarDay((i - dayOfWeek).toString()))
            }
        }
        val startTime = Instant.ofEpochMilli(startInMills)
            .atZone(ZoneId.systemDefault())
        val startDay = startTime.dayOfMonth
        val startMonth = YearMonth.from(startTime)
        val startYear = Year.from(startTime)
        val todayOfMonth = date.dayOfMonth
        val isCurrentMonth = yearMonth == YearMonth.now()
        val isStartYear = startYear == Year.now()
        var workDayInMonth = 0
        var workDayPosition = 0

        createdMonth = findFreeCells(daysInMonthArray)

        setRestDays(date, todayOfMonth, isCurrentMonth)
        addPrevMonthDays(date)
        addNextMonthDays()

        if (startMonth == yearMonth && isStartYear) {
            workDayInMonth = startDay
        } else if (yearMonth > startMonth && Year.now() >= startYear) {
            workDayInMonth = findFirstWorkDay(date, startInMills)
        }

        if (workDayInMonth > 0) {
            workDayPosition = findFirstWordDayPosition(workDayInMonth)
        }

        if (workDayPosition > 0) {
            setWorkDays(workDayPosition, startDay, startMonth, yearMonth)
        }

        if (selectedDays != null)
            addSelectedDay(selectedDays, yearMonth)

        return createdMonth
    }

    private fun findFirstWorkDay(date: LocalDate, startInMills: Long): Int {
        var i = 0
        val difference = TimeUnit.MILLISECONDS.toDays(date.withDayOfMonth(1)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli() - startInMills).toInt() + 1

        if (difference <= 0) {
            createdMonth.forEach {
                if (it.value != "" &&
                    (date.withDayOfMonth(it.value.toInt()).dayOfWeek == DayOfWeek.SATURDAY
                            || date.withDayOfMonth(it.value.toInt()).dayOfWeek == DayOfWeek.SUNDAY)
                ) {
                    it.isWeekend = true
                }
                it.isWorkDay = false
            }
        }

        if (difference % 4 == 0) {
            i = 0
        } else if ((difference + 1) % 4 == 0) {
            i = 1
        } else if ((difference + 2) % 4 == 0) {
            i = 2
        } else if ((difference + 3) % 4 == 0) {
            i = 3
        }

        return 1 + i
    }

    private fun findFirstWordDayPosition(workDayInMonth: Int): Int {
        var workDayPosition = 0
        createdMonth.forEachIndexed { index, dayEntity ->
            if (dayEntity.value == workDayInMonth.toString() && dayEntity.isCurrentMonth) {
                workDayPosition = index
            }
        }
        return workDayPosition
    }

    private fun addPrevMonthDays(date: LocalDate) {
        val prevMonth = date.minusMonths(1)
        val yearMonth = YearMonth.from(prevMonth)
        val daysInMonth = yearMonth.lengthOfMonth()
        var countFreeCells = 0
        for (cell in createdMonth) {
            if (cell.value == "") countFreeCells++
            else break
        }
        for (i in 0 until countFreeCells) {
            createdMonth[i].value = (daysInMonth - countFreeCells + i + 1).toString()
            createdMonth[i].isCurrentMonth = false
        }
    }

    private fun addNextMonthDays() {
        var countFreeCells = 0
        for (cell in createdMonth.reversed()) {
            if (cell.value == "") countFreeCells++
            else break
        }
        val position = createdMonth.size - countFreeCells
        for (i in 0 until countFreeCells) {
            createdMonth[position + i].value = (i + 1).toString()
            createdMonth[position + i].isCurrentMonth = false
        }
    }

    private fun setWorkDays(workDayPosition: Int, startDay: Int, startMonth: YearMonth, yearMonth: YearMonth) {
        for (i in workDayPosition downTo 0 step 4) {
            if (createdMonth[i].value != "" && createdMonth[i].value.toInt() < startDay && startMonth == yearMonth) break
            createdMonth[i].isWorkDay = true
            createdMonth[i + 1].isWorkDay = true
        }
        for (i in workDayPosition + 4 until createdMonth.size step 4) {
            createdMonth[i].isWorkDay = true
            if (i + 1 >=  createdMonth.size) break
            createdMonth[i + 1].isWorkDay = true
        }

    }

    private fun setRestDays(date: LocalDate, todayOfMonth: Int, isCurrentMonth: Boolean) {
        createdMonth.forEach {
            if (it.value != "" &&
                (date.withDayOfMonth(it.value.toInt()).dayOfWeek == DayOfWeek.SATURDAY
                        || date.withDayOfMonth(it.value.toInt()).dayOfWeek == DayOfWeek.SUNDAY)
            ) {
                it.isWeekend = true
            }
            if (it.value == todayOfMonth.toString() && isCurrentMonth) it.isToday = true
            if (it.value == "") it.isWorkDay = false
        }
    }

    private fun addSelectedDay(days: List<DayEntityRemote>, yearMonth: YearMonth) {
        days.forEach { day ->
            val selectedTime = Instant.ofEpochMilli(day.time)
                .atZone(ZoneId.systemDefault())
            val selectedDay = selectedTime.dayOfMonth.toString()
            val selectedMon = YearMonth.from(selectedTime)
            createdMonth.forEach {
                if (it.value == selectedDay && selectedMon == yearMonth && it.isCurrentMonth) it.isWorkDay = day.isWorkDay
            }
        }
    }

    private fun findFreeCells(daysInMonth: MutableList<CalendarDay>): List<CalendarDay> {
        val buffer = mutableListOf<CalendarDay>()
        var isFirstWeekClean = false
        var isLastWeekClean = false
        for (i in 0..6) {
            if (daysInMonth[i].value != "") isFirstWeekClean = true
        }
        for (i in (daysInMonth.size - 7) until daysInMonth.size) {
            if (daysInMonth[i].value != "") isLastWeekClean = true
        }
        if (!isFirstWeekClean) {
            for (i in 7 until daysInMonth.size) buffer.add(daysInMonth[i])
            return buffer
        }
        if (!isLastWeekClean) {
            for (i in 0 until daysInMonth.size - 7) buffer.add(daysInMonth[i])
            return buffer
        }
        buffer.addAll(daysInMonth)
        return buffer
    }
}