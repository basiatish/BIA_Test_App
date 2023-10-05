package com.basiatish.datamodule.repositories.sicklistrepository

import com.basiatish.domain.entities.SickDay
import java.time.LocalDate

interface SickCalendarLocalSource {

    suspend fun getCalendarView(date: LocalDate): List<SickDay>

}