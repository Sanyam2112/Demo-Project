package com.example.vCovid.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.vCovid.data.Repository
import com.example.vCovid.models.country_details.CountryDetailsData
import com.example.vCovid.models.statesData.StatesIndia
import com.example.vCovid.util.NetworkResult
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class DetailViewModel @ViewModelInject constructor(
    private val repository: Repository,
    application: Application,
) : AndroidViewModel(application) {

    var countryDetailsDataResponse : MutableLiveData<NetworkResult<CountryDetailsData>> = MutableLiveData()
    var statesResponse : MutableLiveData<NetworkResult<StatesIndia>> = MutableLiveData()

    fun getStatesData() = viewModelScope.launch {
        getStatesDataSafeCall()
    }

    private suspend fun getStatesDataSafeCall(){
        statesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getStates()
                statesResponse.value = handleStatesResponse(response)
//                Log.i("Sanyam",statesResponse.value!!.data.toString())

            } catch (e: Exception) {
                statesResponse.value = NetworkResult.Error("States not found.")
            }
        } else {
            statesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private fun handleStatesResponse(response: Response<StatesIndia>): NetworkResult<StatesIndia>? {
        return when {
            response.isSuccessful -> {
                val summary = response.body()
                NetworkResult.Success(summary!!)
            }
            else -> {
                NetworkResult.Error(response.message())
            }
        }

    }

    fun getIndividualCountryData(name:String) =  viewModelScope.launch {
        getIndividualCountryDataSafeCall(name)
    }

    private suspend fun getIndividualCountryDataSafeCall(name:String){
        countryDetailsDataResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getDayOneIndividualData(name)
                Log.i("summary", response.body().toString())
                countryDetailsDataResponse.value = handleOneDayResponse(response)

            } catch (e: Exception) {
                countryDetailsDataResponse.value = NetworkResult.Error("Recipes not found.")
            }
        } else {
            countryDetailsDataResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private fun handleOneDayResponse(response: Response<CountryDetailsData>): NetworkResult<CountryDetailsData>? {
        when {
            response.isSuccessful -> {
                val dayonedata = response.body()
                return NetworkResult.Success(dayonedata!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }


}