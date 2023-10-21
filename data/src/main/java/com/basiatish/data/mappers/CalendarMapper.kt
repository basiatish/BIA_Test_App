package com.basiatish.data.mappers

import com.basiatish.data.api.entities.SaveDayRemote
import com.basiatish.domain.entities.CalendarDay

class CalendarMapper {

    fun toSaveDayRemote(time: Long, isWorkDay: Boolean): SaveDayRemote {
        return SaveDayRemote(time, isWorkDay)
    }

    fun toCalendarDay(value: String): CalendarDay {
        return CalendarDay(value)
    }
}