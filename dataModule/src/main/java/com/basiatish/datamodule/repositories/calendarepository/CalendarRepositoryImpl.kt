package com.basiatish.datamodule.repositories.calendarepository

import com.basiatish.domain.common.Result
import com.basiatish.domain.entities.DayEntity
import com.basiatish.domain.repositories.CalendarRepository
import java.time.LocalDate

class CalendarRepositoryImpl(private val remoteSource: CalendarRemoteSource) : CalendarRepository {

    override suspend fun getCalendar(date: LocalDate): Result<List<DayEntity>> {
        return remoteSource.getCalendar(date)
    }

    override suspend fun saveDay(time: Long, isWorkDay: Boolean): Result<String> {
        return remoteSource.saveDay(time, isWorkDay)
    }

}