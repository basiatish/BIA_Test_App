package com.basiatish.data.repositories.profilerepository

import com.basiatish.data.api.BiaApi
import com.basiatish.data.mappers.ProfileMapper
import com.basiatish.domain.common.Result
import com.basiatish.domain.entities.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileRemoteSourceImpl(
    private val service: BiaApi,
    private val mapper: ProfileMapper
) : ProfileRemoteSource {

    override suspend fun loadProfile(): Result<Profile> =
        withContext(Dispatchers.IO) {
            try {
                val response = service.getProfile()
                if (response.isSuccessful) {
                    return@withContext Result.Success(mapper.toProfile(response.body()!!))
                } else {
                    return@withContext Result.Error(Exception(response.message()))
                }
            } catch (e: Exception) {
                return@withContext Result.Error(e)
            }
        }
}