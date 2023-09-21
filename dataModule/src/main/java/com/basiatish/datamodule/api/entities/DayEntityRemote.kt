package com.basiatish.datamodule.api.entities

import com.google.gson.annotations.SerializedName

data class DayEntityRemote(
    @SerializedName("time") val time: Long,
    @SerializedName("isWorkDay") val isWorkDay: Boolean
)