package com.example.vCovid.adapters

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vCovid.data.NetworkHandler
import com.example.vCovid.databinding.CountryRowLayoutBinding
import com.example.vCovid.models.countries.CountriesData
import com.example.vCovid.models.summary.Country
import com.example.vCovid.models.summary.SummaryData
import com.example.vCovid.util.CountryDiffUtil
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL


// Adapter to show countries list on country fragment
class CountriesAdapter : RecyclerView.Adapter<CountriesAdapter.MyViewHolder>() {

    private var countries = emptyList<Country>()

    class MyViewHolder(private val binding: CountryRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Country){
            binding.result = result

            var url = "https://www.countryflags.io/"+result.countryCode+"/shiny/64.png"
            //Log.i("san",binding.imageViewFlag.toString())
            NetworkHandler.FetchFlag(binding.imageViewFlag,url).execute()
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CountryRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentCountry = countries[position]

        holder.bind(currentCountry)
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    fun setData(newData: List<Country>){
        val recipesDiffUtil = CountryDiffUtil(countries, newData)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        countries = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }

}
