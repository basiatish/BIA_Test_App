package com.basiatish.biatestapp.ui.calendar.interfaces

import com.basiatish.domain.entities.CalendarDay

interface OnDayListener {

    fun onDayClick(day: CalendarDay, position: Int)

}