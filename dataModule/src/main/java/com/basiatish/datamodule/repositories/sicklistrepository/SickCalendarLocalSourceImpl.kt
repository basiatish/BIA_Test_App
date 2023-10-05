package com.basiatish.datamodule.repositories.sicklistrepository

import com.basiatish.datamodule.mappers.SickMapper
import com.basiatish.domain.entities.SickDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

class SickCalendarLocalSourceImpl(
    private val mapper: SickMapper
) : SickCalendarLocalSource {

    private var createdMonth: List<SickDay> = listOf()
    private var selectedDate: LocalDate? = null

    override suspend fun getCalendarView(date: LocalDate): List<SickDay> =
        withContext(Dispatchers.IO) {
            //if (date.month == selectedDate?.month) return@withContext createdMonth
            //selectedDate = date
            return@withContext createDaysList(date)
        }

    private fun createDaysList(date: LocalDate): List<SickDay> {
        val daysInMonthArray = mutableListOf<SickDay>()
        val yearMonth = YearMonth.from(date)
        val daysInMonth = yearMonth.lengthOfMonth()
        val firstOfMonth = date.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value
        for (i in 2..43) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add(mapper.toSickDay(""))
            } else {
                daysInMonthArray.add(mapper.toSickDay((i - dayOfWeek).toString()))
            }
        }
        val todayOfMonth = date.dayOfMonth
        val isCurrentMonth = yearMonth == YearMonth.now()

        createdMonth = findFreeCells(daysInMonthArray)

        setUpWeekends(date, todayOfMonth, isCurrentMonth)
        addPrevMonthDays(date)
        addNextMonthDays()

        return createdMonth
    }

    private fun findFreeCells(daysInMonth: MutableList<SickDay>): List<SickDay> {
        val buffer = mutableListOf<SickDay>()
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

    private fun setUpWeekends(date: LocalDate, todayOfMonth: Int, isCurrentMonth: Boolean) {
        createdMonth.forEach {
            if (it.value != "" &&
                (date.withDayOfMonth(it.value.toInt()).dayOfWeek == DayOfWeek.SATURDAY
                        || date.withDayOfMonth(it.value.toInt()).dayOfWeek == DayOfWeek.SUNDAY)
            ) {
                it.isWeekend = true
            }
            if (it.value == todayOfMonth.toString() && isCurrentMonth) it.isToday = true
        }
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
}