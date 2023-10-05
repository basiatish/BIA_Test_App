package com.basiatish.datamodule.api.entities

import com.google.gson.annotations.SerializedName

data class DayEntityHeaderRemote(
    @SerializedName("started_work") val dayStart: Long,
    @SerializedName("days") val days: Map<String, DayEntityRemote>?
)
