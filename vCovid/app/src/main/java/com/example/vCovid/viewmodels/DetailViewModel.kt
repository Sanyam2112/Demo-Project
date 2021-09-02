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
import com.example.vCovid.data.Databasehandler
import com.example.vCovid.data.Repository
import com.example.vCovid.models.dayone.DayOneIndividual
import com.example.vCovid.util.NetworkResult
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class DetailViewModel @ViewModelInject constructor(
    private val repository: Repository,
    application: Application,
) : AndroidViewModel(application) {

    var dayOneIndividualResponse : MutableLiveData<NetworkResult<DayOneIndividual>> = MutableLiveData()

    fun getIndividualCountryData(name:String) =  viewModelScope.launch {
        getIndividualCountryDataSafeCall(name)
    }

    private suspend fun getIndividualCountryDataSafeCall(name:String){
        dayOneIndividualResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getDayOneIndividualData(name)
                Log.i("summary", response.body().toString())
                dayOneIndividualResponse.value = handleOneDayResponse(response)

            } catch (e: Exception) {
                dayOneIndividualResponse.value = NetworkResult.Error("Recipes not found.")
            }
        } else {
            dayOneIndividualResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private fun handleOneDayResponse(response: Response<DayOneIndividual>): NetworkResult<DayOneIndividual>? {
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