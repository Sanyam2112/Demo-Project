package com.example.vCovid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vCovid.data.NetworkHandler
import com.example.vCovid.models.FavouriteCountryModel
import com.example.vCovid.databinding.FavCountryRowLayoutBinding
import com.example.vCovid.models.summary.Country
import com.example.vCovid.util.CountryDiffUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Adapter to show favourite countries list on favourite country fragment
class FavoriteCountryAdapter: RecyclerView.Adapter<FavoriteCountryAdapter.MyViewHolder>() {

    private var favoriteCountries = emptyList<FavouriteCountryModel>()

    class MyViewHolder(private val binding: FavCountryRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favouriteCountryModel: FavouriteCountryModel) {
            val favCountry = stringToCountry(favouriteCountryModel.details)
            binding.favoritesCountry = favCountry

            var url = "https://www.countryflags.io/"+favCountry.countryCode+"/flat/64.png"
            NetworkHandler.FetchFlag(binding.imageViewFlagFav,url).execute()

            binding.executePendingBindings()
        }

        private fun stringToCountry(data: String): Country {
            var gson = Gson()
            val listType = object : TypeToken<Country>() {}.type
            return gson.fromJson(data, listType)
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavCountryRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val selectedCountry = favoriteCountries[position]
        holder.bind(selectedCountry)
    }

    override fun getItemCount(): Int {
        return favoriteCountries.size
    }

    fun setData(newFavoriteCountries: List<FavouriteCountryModel>){
        val favoriteCountryDiffUtil =
            CountryDiffUtil(favoriteCountries, newFavoriteCountries)
        val diffUtilResult = DiffUtil.calculateDiff(favoriteCountryDiffUtil)
        favoriteCountries = newFavoriteCountries
        diffUtilResult.dispatchUpdatesTo(this)
    }

}