package com.example.vCovid.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.vCovid.models.FavouriteCountryModel
import com.example.vCovid.adapters.FavoriteCountryAdapter
import com.example.vCovid.util.NetworkResult


// To use binding adapters for favourite country layout file 
class FavoriteCountryBinding {

    companion object {
        
        @BindingAdapter("setData", "setFavcountries", requireAll = false)
        @JvmStatic
        fun setDataAndViewVisibility(
            view: View,
            mAdapter: FavoriteCountryAdapter?,
            favCountries : NetworkResult<List<FavouriteCountryModel>>
        ) {
            
            var favCountriesList = favCountries.data
            if (favCountriesList.isNullOrEmpty()) {
                when (view) {
                    is ImageView -> {
                        view.visibility = View.VISIBLE
                    }
                    is TextView -> {
                        view.visibility = View.VISIBLE
                    }
                    is RecyclerView -> {
                        view.visibility = View.INVISIBLE
                    }
                }
            } else {
                when (view) {
                    is ImageView -> {
                        view.visibility = View.INVISIBLE
                    }
                    is TextView -> {
                        view.visibility = View.INVISIBLE
                    }
                    is RecyclerView -> {
                        view.visibility = View.VISIBLE
                        mAdapter?.setData(favCountriesList)
                    }
                }
            }
        }

    }

}