package com.example.vCovid.bindingadapters

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.example.vCovid.models.summary.Country
import com.example.vCovid.ui.fragments.country.CountryFragmentDirections
import java.lang.Exception
import java.text.NumberFormat
import java.util.*

class DetailsActivityBinding {

    companion object {

        @BindingAdapter("checkVisibility")
        @JvmStatic
        fun checkVisibility(recyclerView: com.todkars.shimmer.ShimmerRecyclerView, name: String) {
            if(name=="India")
                recyclerView.visibility = View.VISIBLE
            else
                recyclerView.visibility = View.GONE
        }



        @BindingAdapter("setConfirmedStates")
        @JvmStatic
        fun setConfirmedStates(textView: TextView, cases: Int) {

            //textView.text = cases.toString()
            textView.text = NumberFormat.getNumberInstance(Locale.US).format(cases)

        }

        @BindingAdapter("setDeathsStates")
        @JvmStatic
        fun setDeathsStates(textView: TextView, cases: Int) {
            //textView.text = cases.toString()
            textView.text = NumberFormat.getNumberInstance(Locale.US).format(cases)
        }

        @BindingAdapter("confirmedStates", "recoveredStates","deathsStates",requireAll = true)
        @JvmStatic
        fun setActiveStates(textView: TextView, confirmed: Int, recovered: Int, deaths: Int, ) {
            //textView.text = (confirmed-recovered-deaths).toString()
            textView.text = NumberFormat.getNumberInstance(Locale.US).format(confirmed-recovered-deaths)
        }

    }
}