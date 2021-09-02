package com.example.vCovid.data.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface FlagApi {

    @GET("/be/flat/64.png")
    suspend fun getFlag(
    ): Response<ResponseBody>


}