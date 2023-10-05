package com.basiatish.datamodule.mappers

import com.basiatish.datamodule.api.entities.SaveDayRemote
import com.basiatish.domain.entities.CalendarDay
import com.basiatish.domain.entities.SickDay

class CalendarMapper {

    fun toSaveDayRemote(time: Long, isWorkDay: Boolean): SaveDayRemote {
        return SaveDayRemote(time, isWorkDay)
    }

    fun toCalendarDay(value: String): CalendarDay {
        return CalendarDay(value)
    }
}