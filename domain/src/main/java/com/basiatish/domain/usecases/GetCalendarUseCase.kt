package com.basiatish.domain.usecases

import com.basiatish.domain.repositories.CalendarRepository
import java.time.LocalDate

class GetCalendarUseCase(private val calendarRepository: CalendarRepository) {

    suspend fun invoke(date: LocalDate) = calendarRepository.getCalendar(date)

}