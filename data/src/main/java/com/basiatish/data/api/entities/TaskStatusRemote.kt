package com.basiatish.data.api.entities

import com.google.gson.annotations.SerializedName

data class TaskStatusRemote(
    @SerializedName("status") val status: String
)
