package com.basiatish.domain.entities

data class SickDay(
    var value: String,
    var isCurrentMonth: Boolean = true,
    var isToday: Boolean = false,
    var isWeekend: Boolean = false,
    var isSelected: Boolean = false
)