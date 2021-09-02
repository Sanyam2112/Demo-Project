package com.example.vCovid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vCovid.databinding.CountryRowLayoutBinding
import com.example.vCovid.models.summary.Country
import com.example.vCovid.models.summary.SummaryData
import com.example.vCovid.util.CountryDiffUtil


// Adapter to show countries list on country fragment
class CountriesAdapter : RecyclerView.Adapter<CountriesAdapter.MyViewHolder>() {

    private var countries = emptyList<Country>()

    class MyViewHolder(private val binding: CountryRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Country){
            binding.result = result
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