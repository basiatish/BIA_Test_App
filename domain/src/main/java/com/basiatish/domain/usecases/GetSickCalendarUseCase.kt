package com.basiatish.domain.usecases

import com.basiatish.domain.repositories.SickRepository
import java.time.LocalDate

class GetSickCalendarUseCase(private val sickRepository: SickRepository) {

    suspend fun invoke(date: LocalDate) = sickRepository.getCalendarView(date)

}