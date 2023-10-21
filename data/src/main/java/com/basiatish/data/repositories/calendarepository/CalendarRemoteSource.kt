package com.basiatish.data.repositories.calendarepository

import com.basiatish.domain.common.Result
import com.basiatish.domain.entities.CalendarDay
import java.time.LocalDate

interface CalendarRemoteSource {

    suspend fun getCalendar(date: LocalDate): Result<List<CalendarDay>>

    suspend fun saveDay(time: Long, isWorkDay: Boolean): Result<String>

}