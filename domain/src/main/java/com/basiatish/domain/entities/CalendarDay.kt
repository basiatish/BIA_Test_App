package com.basiatish.domain.entities

data class CalendarDay(
    var value: String,
    var isWorkDay: Boolean = false,
    var isWeekend: Boolean = false,
    var isToday: Boolean = false,
    var isCurrentMonth: Boolean = true
)
