package com.basiatish.domain.repositories

import com.basiatish.domain.common.Result
import com.basiatish.domain.entities.SickDay
import com.basiatish.domain.entities.SickListItem
import java.time.LocalDate

interface SickRepository {

    suspend fun getCalendarView(date: LocalDate): List<SickDay>

    suspend fun saveRange(start: Long, end: Long): Result<String>

    suspend fun getSickList(): Result<List<SickListItem>>

    suspend fun closeSickList(start: String, end: String, isClosed: Boolean = true): Result<String>

}