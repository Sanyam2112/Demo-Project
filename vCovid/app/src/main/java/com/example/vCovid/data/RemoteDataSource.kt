package com.example.vCovid.data

import com.example.vCovid.data.network.FlagApi
import com.example.vCovid.data.network.CovidApi
import com.example.vCovid.models.dayone.DayOneIndividual
import com.example.vCovid.models.summary.SummaryData
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val covidApi: CovidApi,
    private val flagApi: FlagApi
) {


    suspend fun getSummary() : Response<SummaryData>{
        return covidApi.getSummary()
    }

    suspend fun getDayOneIndividualData(CountryName:String) : Response<DayOneIndividual>{
        return covidApi.getDayOneIndividualData(CountryName)
    }

    suspend fun getFlag() : Response<ResponseBody>{
        return flagApi.getFlag()
    }


}