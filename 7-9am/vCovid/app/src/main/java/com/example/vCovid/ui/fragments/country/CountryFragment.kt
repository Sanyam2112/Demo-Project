package com.example.vCovid.ui.fragments.country

import android.annotation.SuppressLint
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

import com.google.android.material.chip.Chip


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

    @SuppressLint("UseSwitchCompatOrMaterialCode")
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

        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->

            val checkedChipId = binding.chipGroup.findViewById<Chip>(checkedId)
            Log.i("Sanyam", checkedChipId.id.toString())
            when(checkedChipId.id.toString()) {
                "2131361920" -> {
                    binding.chip1.visibility = View.GONE
                    binding.chip2.visibility = View.VISIBLE
                    binding.chip1Img.visibility = View.GONE
                    binding.chip2Img.visibility = View.VISIBLE
                    sortDataConfirmed(true)
                }
                "2131361921" -> {
                    binding.chip1.visibility = View.VISIBLE
                    binding.chip2.visibility = View.GONE
                    binding.chip1Img.visibility = View.VISIBLE
                    binding.chip2Img.visibility = View.GONE
                    sortDataConfirmed(false)
                }
                "2131361922" -> {
                    binding.chip3.visibility = View.GONE
                    binding.chip4.visibility = View.VISIBLE
                    binding.chip3Img.visibility = View.GONE
                    binding.chip4Img.visibility = View.VISIBLE
                    sortDataDeaths(true)
                }
                "2131361923" -> {
                    binding.chip3.visibility = View.VISIBLE
                    binding.chip4.visibility = View.GONE
                    binding.chip3Img.visibility = View.VISIBLE
                    binding.chip4Img.visibility = View.GONE
                    sortDataDeaths(false)
                }
                "2131361924" -> {
                    binding.chip5.visibility = View.GONE
                    binding.chip6.visibility = View.VISIBLE
                    binding.chip5Img.visibility = View.GONE
                    binding.chip6Img.visibility = View.VISIBLE
                    sortDataActive(true)
                }
                "2131361925" -> {
                    binding.chip5.visibility = View.VISIBLE
                    binding.chip6.visibility = View.GONE
                    binding.chip5Img.visibility = View.VISIBLE
                    binding.chip6Img.visibility = View.GONE
                    sortDataActive(false)
                }

            }
        }

        return binding.root
    }

    private fun sortDataConfirmed(flag:Boolean){
        countryViewModel.summaryResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { it ->
                        var sortedCountriesListConfirmed: List<Country>
                        if(flag){
                            sortedCountriesListConfirmed = it.countries.sortedWith(compareBy{it.totalConfirmed})
                        }
                        else
                        {
                            sortedCountriesListConfirmed = it.countries.sortedWith(compareBy{it.totalConfirmed}).reversed()
                        }
                        mAdapterCountries.setData(sortedCountriesListConfirmed) }
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

    private fun sortDataDeaths(flag:Boolean){
        countryViewModel.summaryResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { it ->
                        var sortedCountriesListDeaths :List<Country>
                        if(flag){
                            sortedCountriesListDeaths = it.countries.sortedWith(compareBy{it.totalDeaths})
                        }
                        else
                        {
                            sortedCountriesListDeaths = it.countries.sortedWith(compareBy{it.totalDeaths}).reversed()
                        }

                        mAdapterCountries.setData(sortedCountriesListDeaths) }
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

    private fun sortDataActive(flag:Boolean){
        countryViewModel.summaryResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { it ->
                        var sortedCountriesListActive :List<Country>
                        if(flag){
                            sortedCountriesListActive = it.countries.sortedWith(compareBy{it.totalConfirmed-it.totalDeaths-it.totalRecovered})
                        }
                        else
                        {
                            sortedCountriesListActive = it.countries.sortedWith(compareBy{it.totalConfirmed-it.totalDeaths-it.totalRecovered}).reversed()
                        }

                        mAdapterCountries.setData(sortedCountriesListActive) }
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
        if(query != null) {
            filterResult(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
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
                    response.data?.let {
                        val sortedData = it.countries
//                        val sortedDataConf = it.countries.sortedWith(compareBy{it.totalConfirmed}).reversed()
//                        val sortedDataDeaths = sortedDataConf.sortedWith(compareBy{it.totalDeaths}).reversed()
                        mAdapterCountries.setData(sortedData) }
                        Log.i("SanyamJain",response.data!!.countries[28].totalRecovered.toString())
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