package com.example.vCovid.models.statesData


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("regional")
    val regional: List<Regional>,
    @SerializedName("summary")
    val summary: Summary
)