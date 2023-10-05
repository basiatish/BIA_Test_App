package com.basiatish.domain.usecases

import com.basiatish.domain.repositories.ProfileRepository

class LoadProfileUseCase(private val profileRepository: ProfileRepository) {

    suspend fun invoke() = profileRepository.loadProfile()

}