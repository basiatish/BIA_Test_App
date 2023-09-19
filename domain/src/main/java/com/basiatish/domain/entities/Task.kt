package com.basiatish.domain.entities

data class Task(
    val id: Int,
    val status: String,
    val cargoType: String,
    val city: String,
    val date: String,
    val time: String,
    val startPoint: String,
    val endPoint: String,
    val bodyType: String,
    val orderDetails: String,
    val payDetails: String,
    val phone: String,
    val name: String
)
