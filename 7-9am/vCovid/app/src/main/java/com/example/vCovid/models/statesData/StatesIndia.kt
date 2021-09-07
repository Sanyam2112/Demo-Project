package com.example.vCovid.models.statesData


import com.example.vCovid.models.statesData.Data
import com.google.gson.annotations.SerializedName

data class StatesIndia(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("lastOriginUpdate")
    val lastOriginUpdate: String,
    @SerializedName("lastRefreshed")
    val lastRefreshed: String,
    @SerializedName("success")
    val success: Boolean
)