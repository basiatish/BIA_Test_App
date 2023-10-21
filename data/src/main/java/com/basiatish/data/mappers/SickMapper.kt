package com.basiatish.data.mappers

import com.basiatish.data.api.entities.SickListStatus
import com.basiatish.data.api.entities.SickRemote
import com.basiatish.domain.entities.SickDay
import com.basiatish.domain.entities.SickListItem

class SickMapper {

    fun toSickDay(value: String): SickDay {
        return SickDay(value)
    }

    fun toSickRemote(
        start: Long, end: Long, startDate: String, endDate: String, isClosed: Boolean
    ): SickRemote {
        return SickRemote(start, end, startDate, endDate, isClosed)
    }

    fun toSickItem(value: SickRemote): SickListItem {
        return SickListItem(
            startDate = value.startDate,
            endDate = value.endDate,
            isClosed = value.isClosed
        )
    }

    fun toSickStatus(isClosed: Boolean): SickListStatus {
        return SickListStatus(isClosed)
    }
}