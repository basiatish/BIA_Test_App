package com.basiatish.domain.usecases

import com.basiatish.domain.repositories.SickRepository

class CloseSickListUseCase(private val sickRepository: SickRepository) {

    suspend fun invoke(start: String, end: String, isClosed: Boolean = true) = sickRepository.closeSickList(start, end, isClosed)

}