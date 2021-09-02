package com.example.vCovid.ui.fragments.country

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vCovid.viewmodels.CountryViewModel
import com.example.vCovid.R
import com.example.vCovid.adapters.CountriesAdapter
import com.example.vCovid.databinding.FragmentCountryBinding
import com.example.vCovid.models.summary.Country
import com.example.vCovid.util.NetworkResult

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*
import kotlin.collections.ArrayList

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CountryFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentCountryBinding? = null
    private val binding get() = _binding!!
    private lateinit var countryViewModel: CountryViewModel
    private val mAdapterCountries by lazy {CountriesAdapter()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        countryViewModel = ViewModelProvider(requireActivity()).get(CountryViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCountryBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = countryViewModel
        setHasOptionsMenu(true)
        setupRecyclerViewForCountries()
        requestApiDataForCountries()
        return binding.root
    }

    private fun setupRecyclerViewForCountries() {
        binding.countriesRecyclerView.adapter = mAdapterCountries
        binding.countriesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.country_menu, menu)
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Log.i("hi all",query!!)
        if(query != null) {
            filterResult(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        Log.i("hi",query!!)
        if(query != null) {
            filterResult(query.toLowerCase(Locale.getDefault()))
        }
        return true
    }

    private fun filterResult(query: String?) {
        countryViewModel.summaryResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { it ->
                        var localData = it
                        var countriesList : ArrayList<Country> = arrayListOf()

                        localData.countries.forEach {
                            if(it.country.toLowerCase(Locale.getDefault()).contains(query!!)) {
                                countriesList.add(it)
                            }
                        }
                        mAdapterCountries.setData(countriesList) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })
    }

    private fun requestApiDataForCountries() {
        countryViewModel.getSummaryCountries()
        countryViewModel.summaryResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapterCountries.setData(it.countries) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })
    }

    private fun showShimmerEffect() {
        binding.countriesRecyclerView.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding.countriesRecyclerView.hideShimmer()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}