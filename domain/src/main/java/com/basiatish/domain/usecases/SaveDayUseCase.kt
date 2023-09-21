package com.basiatish.domain.usecases

import com.basiatish.domain.repositories.CalendarRepository
import java.time.LocalDate

class SaveDayUseCase(private val calendarRepository: CalendarRepository) {

    suspend fun invoke(time: Long, isWorkDay: Boolean) = calendarRepository.saveDay(time, isWorkDay)

}