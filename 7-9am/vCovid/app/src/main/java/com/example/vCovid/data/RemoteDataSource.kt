package com.example.vCovid.data

import com.example.vCovid.data.network.IndianStatesAPI
import com.example.vCovid.data.network.CovidApi
import com.example.vCovid.models.country_details.CountryDetailsData
import com.example.vCovid.models.statesData.StatesIndia
import com.example.vCovid.models.summary.SummaryData
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val covidApi: CovidApi,
    private val indianStatesAPI: IndianStatesAPI
) {


    suspend fun getSummary() : Response<SummaryData>{
        return covidApi.getSummary()
    }

    suspend fun getDayOneIndividualData(CountryName:String) : Response<CountryDetailsData>{
        return covidApi.getDayOneIndividualData(CountryName)
    }

    suspend fun getStates() : Response<StatesIndia>{
        return indianStatesAPI.getStates()
    }


}