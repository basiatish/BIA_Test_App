package com.basiatish.datamodule.repositories.sicklistrepository

import com.basiatish.domain.common.Result
import com.basiatish.domain.entities.SickListItem

interface SickListRemoteSource {

    suspend fun saveRange(start: Long, end: Long): Result<String>

    suspend fun getSickList(): Result<List<SickListItem>>

    suspend fun closeSickList(start: String, end: String, isClosed: Boolean = true): Result<String>

}