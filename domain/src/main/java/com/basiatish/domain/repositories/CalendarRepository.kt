package com.basiatish.domain.repositories

import com.basiatish.domain.common.Result
import com.basiatish.domain.entities.CalendarDay
import java.time.LocalDate

interface CalendarRepository {

    suspend fun getCalendar(date: LocalDate): Result<List<CalendarDay>>

    suspend fun saveDay(time: Long, isWorkDay: Boolean): Result<String>

}