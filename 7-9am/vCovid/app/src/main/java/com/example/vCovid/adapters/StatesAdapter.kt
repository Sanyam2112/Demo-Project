package com.example.vCovid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vCovid.data.NetworkHandler
import com.example.vCovid.databinding.CountryRowLayoutBinding
import com.example.vCovid.databinding.StatesRowLayoutBinding
import com.example.vCovid.models.statesData.Regional
import com.example.vCovid.models.summary.Country
import com.example.vCovid.util.CountryDiffUtil

class StatesAdapter : RecyclerView.Adapter<StatesAdapter.MyViewHolder>() {

    private var states = emptyList<Regional>()

    class MyViewHolder(private val binding: StatesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Regional){
            binding.result = result
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = StatesRowLayoutBinding.inflate(layoutInflater, parent, false)
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
        val currentState = states[position]

        holder.bind(currentState)
    }

    override fun getItemCount(): Int {
        return states.size
    }

    fun setData(newData: List<Regional>){
        val recipesDiffUtil = CountryDiffUtil(states, newData)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        states = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }

}