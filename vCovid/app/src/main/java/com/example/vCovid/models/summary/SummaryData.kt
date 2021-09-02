package com.example.vCovid.models.summary


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class SummaryData(
    @SerializedName("Countries")
    val countries: @RawValue List<Country>,
    @SerializedName("Date")
    val date: String,
    @SerializedName("Global")
    val global: Global,
    @SerializedName("ID")
    val iD: String,
    @SerializedName("Message")
    val message: String
): Parcelable