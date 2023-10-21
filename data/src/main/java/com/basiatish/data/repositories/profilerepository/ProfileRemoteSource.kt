package com.basiatish.data.repositories.profilerepository

import com.basiatish.domain.common.Result
import com.basiatish.domain.entities.Profile

interface ProfileRemoteSource {

    suspend fun loadProfile(): Result<Profile>

}