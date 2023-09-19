package com.basiatish.datamodule.api.entities

import com.google.gson.annotations.SerializedName

data class TaskStatusRemote(
    @SerializedName("status") val status: String
)
