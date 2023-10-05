package com.basiatish.domain.usecases

import com.basiatish.domain.repositories.SickRepository

class SaveRangeUseCase(private val sickRepository: SickRepository) {

    suspend fun invoke(start: Long, end: Long) = sickRepository.saveRange(start, end)

}