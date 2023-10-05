package com.basiatish.domain.entities

data class Profile(
    val userName: String,
    val userType: String,
    val compNumber: String,
    val phone: String,
    val citizenship: String,
    val carType: String,
    val carNumber: String,
    val photoUrl: String
)
