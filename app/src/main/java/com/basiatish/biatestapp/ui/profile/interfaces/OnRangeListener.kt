package com.basiatish.biatestapp.ui.profile.interfaces

import com.basiatish.domain.entities.SickDay

interface OnRangeListener {

    fun onDayClick(day: SickDay, position: Int)

}