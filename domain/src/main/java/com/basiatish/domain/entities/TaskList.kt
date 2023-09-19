package com.basiatish.domain.entities

data class TaskList(
    val id: Int,
    val status: String,
    val cargoType: String,
    val date: String,
    val time: String,
    val startPoint: String,
    val endPoint: String,
    val orderDetails: String,
    val payDetails: String
)
