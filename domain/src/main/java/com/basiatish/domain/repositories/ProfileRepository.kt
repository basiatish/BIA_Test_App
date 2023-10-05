package com.basiatish.domain.repositories

import com.basiatish.domain.common.Result
import com.basiatish.domain.entities.Profile

interface ProfileRepository {

    suspend fun loadProfile(): Result<Profile>

}