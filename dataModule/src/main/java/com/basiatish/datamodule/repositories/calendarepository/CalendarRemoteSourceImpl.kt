package com.basiatish.datamodule.repositories.calendarepository

import com.basiatish.datamodule.api.BiaApi
import com.basiatish.datamodule.api.entities.DayEntityRemote
import com.basiatish.datamodule.mappers.CalendarMapper
import com.basiatish.domain.common.Result
import com.basiatish.domain.entities.DayEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.util.concurrent.TimeUnit

class CalendarRemoteSourceImpl(
    private val service: BiaApi,
    private val mapper: CalendarMapper
) : CalendarRemoteSource {

    override suspend fun getCalendar(date: LocalDate): Result<List<DayEntity>> =
        withContext(Dispatchers.IO) {
            try {
                val response = service.getCalendar()
                if (response.isSuccessful) {
                    val responseResult = response.body()!!
                    //val collectionType = object : TypeToken<List<DayEntityHeaderRemote>>() {}.type
                    //val list = Gson().fromJson(Gson().toJson(responseResult), collectionType) as List<DayEntityHeaderRemote>
                    //println(list.size)
                    return@withContext Result.Success(daysInMonthArray(date, responseResult.dayStart, responseResult.restDay))
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
                val response = service.saveDay(mapper.toSaveDayRemote(time, isWorkDay))
                if (response.isSuccessful) {
                    return@withContext Result.Success("Success")
                } else {
                    return@withContext Result.Error(Exception(response.message()))
                }
            } catch (e: Exception) {
                return@withContext Result.Error(e)
            }
        }

    private var cleanMonth: List<DayEntity> = listOf()

    private fun daysInMonthArray(date: LocalDate, startInMills: Long, selectedDays: DayEntityRemote): List<DayEntity> {
        val daysInMonthArray = mutableListOf<DayEntity>()
        val yearMonth = YearMonth.from(date)
        val daysInMonth = yearMonth.lengthOfMonth()
        val firstOfMonth = date.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value
        for (i in 2..43) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add(mapper.toDayEntity(""))
            } else {
                daysInMonthArray.add(mapper.toDayEntity((i - dayOfWeek).toString()))
            }
        }
        val startDay = Instant.ofEpochMilli(startInMills)
            .atZone(ZoneId.systemDefault()).dayOfMonth
        val startMonth = YearMonth.from(Instant.ofEpochMilli(startInMills)
            .atZone(ZoneId.systemDefault()))
        cleanMonth = findFreeCells(daysInMonthArray)
        val todayOfMonth = 21
        val isCurrentMonth = yearMonth == YearMonth.now()
        var i = 0
        var difference = TimeUnit.MILLISECONDS
            .toDays((System.currentTimeMillis() - startInMills)).toInt()

        if (!isCurrentMonth) {
            difference = TimeUnit.MILLISECONDS.toDays(date
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli() - startInMills).toInt() + 1
        }

        if (difference <= 0) {
            cleanMonth.forEach {
                if (it.value != "" &&
                    (date.withDayOfMonth(it.value.toInt()).dayOfWeek == DayOfWeek.SATURDAY
                            || date.withDayOfMonth(it.value.toInt()).dayOfWeek == DayOfWeek.SUNDAY)
                ) {
                    it.isWeekend = true
                }
                it.isWorkDay = false
            }
            return cleanMonth
        }

        if (difference % 4 == 0) {
            i = 0
        } else if ((difference + 1) % 4 == 0 && todayOfMonth + 1 < daysInMonth) {
            i = 1
        } else if ((difference + 2) % 4 == 0 && todayOfMonth + 2 < daysInMonth) {
            i = 2
        } else if ((difference - 3) % 4 == 0 && todayOfMonth - 3 >= 4) {
            i = -3
        } else if ((difference - 2) % 4 == 0 && todayOfMonth - 2 >= 3) {
            i = -2
        } else if ((difference - 1) % 4 == 0 && todayOfMonth - 1 >= 2) {
            i = -1
        }

        val workDayInMonth = todayOfMonth + i

        var workDayPosition = 0
        cleanMonth.forEachIndexed { index, dayEntity ->
            if (dayEntity.value == workDayInMonth.toString()) {
                workDayPosition = index
            }
        }

        setWorkDays(workDayPosition, startDay, startMonth, yearMonth)
        setRestDays(date, todayOfMonth, isCurrentMonth)
        addSelectedDay(selectedDays.time, selectedDays.isWorkDay, yearMonth)

        return cleanMonth
    }

    private fun setWorkDays(workDayPosition: Int, startDay: Int, startMonth: YearMonth, yearMonth: YearMonth) {
        for (i in workDayPosition downTo 0 step 4) {
            if (cleanMonth[i].value != "" && cleanMonth[i].value.toInt() < startDay && startMonth == yearMonth) break
            cleanMonth[i].isWorkDay = true
            cleanMonth[i + 1].isWorkDay = true
        }
        for (i in workDayPosition + 4 until cleanMonth.size - 1 step 4) {
            cleanMonth[i].isWorkDay = true
            cleanMonth[i + 1].isWorkDay = true
        }
    }

    private fun setRestDays(date: LocalDate, todayOfMonth: Int, isCurrentMonth: Boolean) {
        cleanMonth.forEach {
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

    private fun addSelectedDay(time: Long, isWorkDay: Boolean, yearMonth: YearMonth) {
        val selectedTime = Instant.ofEpochMilli(time)
            .atZone(ZoneId.systemDefault())
        val selectedDay = selectedTime.dayOfMonth.toString()
        val selectedMon = YearMonth.from(selectedTime)
        cleanMonth.forEach {
            if (it.value == selectedDay && selectedMon == yearMonth) it.isWorkDay = isWorkDay
        }
    }

    private fun findFreeCells(daysInMonth: MutableList<DayEntity>): List<DayEntity> {
        val buffer = mutableListOf<DayEntity>()
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