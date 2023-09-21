package com.basiatish.datamodule.api.entities

import com.google.gson.annotations.SerializedName

data class DayEntityHeaderRemote(
    @SerializedName("started_work") val dayStart: Long,
    @SerializedName("day") val restDay: DayEntityRemote
)
