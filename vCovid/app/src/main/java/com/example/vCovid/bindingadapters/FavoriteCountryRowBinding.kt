package com.example.vCovid.bindingadapters

import android.util.Log
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.example.vCovid.models.summary.Country
import com.example.vCovid.ui.fragments.favorites.FavoriteCountryFragmentDirections
import java.lang.Exception


// To use binding adapters for favourite country row layout file
class FavoriteCountryRowBinding {

    companion object {
        @BindingAdapter("onFavCountryClickListener")
        @JvmStatic
        fun onFavCountryClickListener(countryRowLayout: ConstraintLayout, result: Country) {

            countryRowLayout.setOnClickListener {
                try {
                    val action = FavoriteCountryFragmentDirections.actionFavouriteFragmentToDetailsActivity(result)
                    countryRowLayout.findNavController().navigate(action)
                } catch (e: Exception) {
                    Log.d("onFavCountryClickListener", e.toString())
                }
            }
        }

        @BindingAdapter("setConfirmedFav")
        @JvmStatic
        fun setConfirmed(textView: TextView, cases: Int) {
            textView.text = cases.toString()
        }

        @BindingAdapter("setDeathsFav")
        @JvmStatic
        fun setDeathsFav(textView: TextView, cases: Int) {
            textView.text = cases.toString()
        }

        @BindingAdapter("confirmedFav", "recoveredFav","deathsFav",requireAll = true)
        @JvmStatic
        fun setActive(textView: TextView, confirmed: Int, recovered: Int, deaths: Int, ) {
            textView.text = (confirmed-recovered-deaths).toString()
        }


    }
}