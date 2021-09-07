package com.example.vCovid.models.countries


import com.google.gson.annotations.SerializedName

data class CountriesDataItem(
    @SerializedName("Country")
    val country: String,
    @SerializedName("ISO2")
    val iSO2: String,
    @SerializedName("Slug")
    val slug: String
)