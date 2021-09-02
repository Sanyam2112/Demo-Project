package com.example.vCovid.bindingadapters

import android.util.Log
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.example.vCovid.models.summary.Country
import com.example.vCovid.ui.fragments.country.CountryFragmentDirections
import java.lang.Exception

// To use binding adapters for country row layout file 
class CountryRowBinding {

    companion object {
        
        @BindingAdapter("onCountryClickListener")
        @JvmStatic
        fun onCountryClickListener(countryRowLayout: ConstraintLayout, result: Country) {
            Log.d("onRecipeClickListener", "CALLED")
            countryRowLayout.setOnClickListener {
                try {
                    val action =
                        CountryFragmentDirections.actionCountryFragmentToDetailsActivity(result)
                    countryRowLayout.findNavController().navigate(action)
                } catch (e: Exception) {
                    Log.d("onRecipeClickListener", e.toString())
                }
            }
        }


        @BindingAdapter("setConfirmed")
        @JvmStatic
        fun setConfirmed(textView: TextView, cases: Int) {
            textView.text = cases.toString()
        }

        @BindingAdapter("setDeaths")
        @JvmStatic
        fun setDeaths(textView: TextView, cases: Int) {
            textView.text = cases.toString()
        }

        @BindingAdapter("confirmed", "recovered","deaths",requireAll = true)
        @JvmStatic
        fun setActive(textView: TextView, confirmed: Int, recovered: Int, deaths: Int, ) {
            textView.text = (confirmed-recovered-deaths).toString()
        }



    }

}