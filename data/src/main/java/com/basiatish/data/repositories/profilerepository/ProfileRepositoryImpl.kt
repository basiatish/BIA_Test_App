package com.basiatish.data.repositories.profilerepository

import com.basiatish.domain.common.Result
import com.basiatish.domain.entities.Profile
import com.basiatish.domain.repositories.ProfileRepository

class ProfileRepositoryImpl(private val profileRemoteSource: ProfileRemoteSource) : ProfileRepository {

    override suspend fun loadProfile(): Result<Profile> {
        return profileRemoteSource.loadProfile()
    }

}