package com.basiatish.data.api.entities

data class SickRemote(
    val start: Long,
    val end: Long,
    val startDate: String,
    val endDate: String,
    val isClosed: Boolean
)
