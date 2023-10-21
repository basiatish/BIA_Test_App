package com.basiatish.data.api.entities

import com.google.gson.annotations.SerializedName

data class ProfileRemote(
    @SerializedName("name") val userName: String,
    @SerializedName("user_type") val userType: String,
    @SerializedName("company_number") val compNumber: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("citizen") val citizenship: String,
    @SerializedName("car") val carType: String,
    @SerializedName("car_num") val carNumber: String,
    @SerializedName("image_url") val photoUrl: String
)
