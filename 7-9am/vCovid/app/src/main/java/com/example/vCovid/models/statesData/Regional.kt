package com.example.vCovid.models.statesData


import com.google.gson.annotations.SerializedName

data class Regional(
    @SerializedName("confirmedCasesForeign")
    val confirmedCasesForeign: Int,
    @SerializedName("confirmedCasesIndian")
    val confirmedCasesIndian: Int,
    @SerializedName("deaths")
    val deaths: Int,
    @SerializedName("discharged")
    val discharged: Int,
    @SerializedName("loc")
    val loc: String,
    @SerializedName("totalConfirmed")
    val totalConfirmed: Int
)