package com.basiatish.data.api.entities

import com.google.gson.annotations.SerializedName

data class TaskRemote(
    @SerializedName("id") val id: Int,
    @SerializedName("status") val status: String,
    @SerializedName("cargo_type") val cargoType: String,
    @SerializedName("city") val city: String,
    @SerializedName("date") val date: String,
    @SerializedName("time") val time: String,
    @SerializedName("start_point") val startPoint: String,
    @SerializedName("end_point") val endPoint: String,
    @SerializedName("body_type") val bodyType: String,
    @SerializedName("order_details") val orderDetails: String,
    @SerializedName("pay_details") val payDetails: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("name") val name: String
)

