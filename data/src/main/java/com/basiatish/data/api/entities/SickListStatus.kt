package com.basiatish.data.api.entities

import com.google.gson.annotations.SerializedName

data class SickListStatus(
    @SerializedName("isClosed") private val status: Boolean
)
