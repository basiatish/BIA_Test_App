package com.basiatish.data.api.entities

import com.google.gson.annotations.SerializedName

data class SaveDayRemote(
    @SerializedName("time") val time: Long,
    @SerializedName("is_work_day") val isWorkDay: Boolean
)
