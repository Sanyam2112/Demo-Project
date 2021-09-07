package com.example.vCovid.models.statesData


import com.google.gson.annotations.SerializedName

data class Summary(
    @SerializedName("confirmedButLocationUnidentified")
    val confirmedButLocationUnidentified: Int,
    @SerializedName("confirmedCasesForeign")
    val confirmedCasesForeign: Int,
    @SerializedName("confirmedCasesIndian")
    val confirmedCasesIndian: Int,
    @SerializedName("deaths")
    val deaths: Int,
    @SerializedName("discharged")
    val discharged: Int,
    @SerializedName("total")
    val total: Int
)