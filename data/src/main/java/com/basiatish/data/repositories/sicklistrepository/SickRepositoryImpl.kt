package com.basiatish.data.repositories.sicklistrepository

import com.basiatish.domain.common.Result
import com.basiatish.domain.entities.SickDay
import com.basiatish.domain.entities.SickListItem
import com.basiatish.domain.repositories.SickRepository
import java.time.LocalDate

class SickRepositoryImpl(
    private val sickCalendarLocalSource: SickCalendarLocalSource,
    private val sickListRemoteSource: SickListRemoteSource
) : SickRepository {

    override suspend fun getCalendarView(date: LocalDate): List<SickDay> {
        return sickCalendarLocalSource.getCalendarView(date)
    }

    override suspend fun saveRange(start: Long, end: Long): Result<String> {
        return sickListRemoteSource.saveRange(start, end)
    }

    override suspend fun getSickList(): Result<List<SickListItem>> {
        return sickListRemoteSource.getSickList()
    }

    override suspend fun closeSickList(
        start: String,
        end: String,
        isClosed: Boolean
    ): Result<String> {
        return sickListRemoteSource.closeSickList(start, end, isClosed)
    }

}