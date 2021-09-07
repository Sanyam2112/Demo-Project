package com.example.vCovid.data.network

import com.example.vCovid.models.statesData.StatesIndia
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface IndianStatesAPI {

    @GET("stats/latest")
    suspend fun getStates(
    ): Response<StatesIndia>

}