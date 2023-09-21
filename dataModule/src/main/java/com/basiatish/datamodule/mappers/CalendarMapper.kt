package com.basiatish.datamodule.mappers

import com.basiatish.datamodule.api.entities.DayEntityRemote
import com.basiatish.datamodule.api.entities.SaveDayRemote
import com.basiatish.domain.entities.DayEntity

class CalendarMapper {

    fun toSaveDayRemote(time: Long, isWorkDay: Boolean): SaveDayRemote {
        return SaveDayRemote(time, isWorkDay)
    }

    fun toDayEntity(value: String): DayEntity {
        return DayEntity(value)
    }

}