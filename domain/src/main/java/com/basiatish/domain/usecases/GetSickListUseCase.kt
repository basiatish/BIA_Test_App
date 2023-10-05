package com.basiatish.domain.usecases

import com.basiatish.domain.repositories.SickRepository

class GetSickListUseCase(private val sickRepository: SickRepository) {

    suspend fun invoke() = sickRepository.getSickList()

}