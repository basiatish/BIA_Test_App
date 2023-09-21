package com.basiatish.domain.entities

data class DayEntity(
    val value: String,
    var isWorkDay: Boolean = false,
    var isWeekend: Boolean = false,
    var isToday: Boolean = false
)
