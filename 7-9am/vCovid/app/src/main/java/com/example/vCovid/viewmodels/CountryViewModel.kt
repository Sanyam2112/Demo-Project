package com.example.vCovid.viewmodels

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.vCovid.data.Databasehandler
import com.example.vCovid.models.FavouriteCountryModel
import com.example.vCovid.data.Repository
import com.example.vCovid.models.summary.Country
import com.example.vCovid.models.summary.SummaryData
import com.example.vCovid.util.NetworkResult
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import android.R
import android.graphics.drawable.BitmapDrawable

import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

import android.graphics.drawable.Drawable
import com.example.vCovid.models.statesData.StatesIndia
import java.io.ByteArrayOutputStream


class CountryViewModel @ViewModelInject constructor(
    private val repository: Repository,
    application: Application,
    private val dbHandler: Databasehandler
) : AndroidViewModel(application) {

    var favouriteCountriesResponse :MutableLiveData<NetworkResult<List<FavouriteCountryModel>>> = MutableLiveData()
    var favcon : ArrayList<FavouriteCountryModel>? = null
    fun fetchFavoriteCountries() {
        favcon = dbHandler.fetchFavCountries()
        favouriteCountriesResponse.value = NetworkResult.Success(favcon!!)


    }



    fun filterFavCountries(query:String?) {

        var countriesList : ArrayList<FavouriteCountryModel> = arrayListOf()

        favcon!!.forEach {
            if(it.name.toLowerCase(Locale.getDefault()).contains(query!!))
            {
                countriesList.add(it)
            }
        }
        favouriteCountriesResponse.value = NetworkResult.Success(countriesList)
    }

    fun insertFavCountry(name:String, countryDetails:Country) {
        var gson = Gson()
        var details = gson.toJson(countryDetails)
        dbHandler.addFavouriteCountry(FavouriteCountryModel(0, name, details))
    }

    fun deleteFavCountry(id:Int) {
        dbHandler.deleteFavouriteCountry(id)
    }

    /** RETROFIT */

    var summaryResponse : MutableLiveData<NetworkResult<SummaryData>> = MutableLiveData()

    fun getSummaryCountries() = viewModelScope.launch {
        getSummaryCountriesSafeCall()
    }

    private suspend fun getSummaryCountriesSafeCall() {
        summaryResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getSummary()
                summaryResponse.value = handleSummaryResponse(response)

//                val Sanyam = repository.remote.getFlag()
//                Log.i("Sanyam",Sanyam.body().toString())

            } catch (e: Exception) {
                summaryResponse.value = NetworkResult.Error("Countries not found.")
            }
        } else {
            summaryResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }



    private fun handleSummaryResponse(response: Response<SummaryData>): NetworkResult<SummaryData>? {
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