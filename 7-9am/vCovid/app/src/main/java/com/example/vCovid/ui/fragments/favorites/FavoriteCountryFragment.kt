package com.example.vCovid.ui.fragments.favorites

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vCovid.R
import com.example.vCovid.adapters.FavoriteCountryAdapter
import com.example.vCovid.databinding.FragmentFavoriteCountryBinding
import com.example.vCovid.viewmodels.CountryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteCountryFragment : Fragment(), SearchView.OnQueryTextListener {

    private val mAdapter: FavoriteCountryAdapter by lazy { FavoriteCountryAdapter() }
    private val countryViewModel: CountryViewModel by viewModels()
    private var _binding: FragmentFavoriteCountryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFavoriteCountryBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = countryViewModel
        setHasOptionsMenu(true)
        binding.mAdapter = mAdapter
        setupRecyclerView(binding.favoriteCountryRecyclerView)
        countryViewModel.fetchFavoriteCountries()
        return binding.root
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favourite_country_menu, menu)
        val search = menu.findItem(R.id.menu_search_fav)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null) {
//            searchApiData(query)
            Log.i("Fav hi all",query)
            countryViewModel.filterFavCountries(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        Log.i("Fav hi ",query!!)

        //countryViewModel.favouriteCountriesResponse.observe()
        countryViewModel.filterFavCountries(query)
        return true
    }
}