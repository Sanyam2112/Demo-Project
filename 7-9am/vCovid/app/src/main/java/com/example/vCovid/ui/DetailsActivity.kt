package com.example.vCovid.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.navigation.navArgs
import com.example.vCovid.R
import com.example.vCovid.viewmodels.CountryViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_details.*
import androidx.databinding.DataBindingUtil
import com.example.vCovid.databinding.ActivityDetailsBinding
import com.example.vCovid.models.summary.Country
import com.example.vCovid.util.NetworkResult
import com.example.vCovid.viewmodels.DetailViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vCovid.adapters.StatesAdapter


@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val args by navArgs<DetailsActivityArgs>()
    private val countryViewModel: CountryViewModel by viewModels()
    private val detailViewModel:DetailViewModel by viewModels()
    private var countrySaved = false
    private var savedCountryId = 0
    private val mAdapter: StatesAdapter by lazy { StatesAdapter() }

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val binding: ActivityDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_details)
        binding.countryDetails  = args.result
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        getDataForThisCountry(args.result.country)

        getStatesDataforIndia()


        //binding.mAdapter = mAdapter
        setupRecyclerView(binding.statesRecyclerView)

//        var url = "https://www.countryflags.io/"+ args.result.countryCode + "/flat/64.png"
//        val imageview = findViewById<View>(R.id.imageView1) as ImageView
//        NetworkHandler.FetchFlag(imageview,url).execute()

    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun getStatesDataforIndia()
    {
        detailViewModel.getStatesData()
        detailViewModel.statesResponse.observe(this, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    Log.i("Sanyam", response.data.toString())
                    response.data?.let { mAdapter.setData(it.data.regional) }
//                   data to show graphs
                }
                is NetworkResult.Error -> {
                    Toast.makeText(
                        this,
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

    }

    private fun getDataForThisCountry(name:String) {

        detailViewModel.getIndividualCountryData(name)
        detailViewModel.countryDetailsDataResponse.observe(this, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    Log.i("DetailsSummary", response.data.toString())
//                    response.data?.let { mAdapterCountries.setData(it) }
//                   data to show graphs
                }
                is NetworkResult.Error -> {
                    Toast.makeText(
                        this,
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        val menuItem = menu?.findItem(R.id.save_to_favorites_menu)
        checkSavedCountries(menuItem!!)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.save_to_favorites_menu && !countrySaved) {
            saveToFavorites(item)

        } else if (item.itemId == R.id.save_to_favorites_menu && countrySaved) {
            removeFromFavorites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun stringToCountry(data: String): Country {
        var gson = Gson()
        val listType = object : TypeToken<Country>() {}.type
        return gson.fromJson(data, listType)
    }

    private fun checkSavedCountries(menuItem: MenuItem) {

        //find solution for it.
        countryViewModel.fetchFavoriteCountries()
        countryViewModel.favouriteCountriesResponse!!.observe(this, { favoriteCountryList ->
            try {
                for (savedRecipe in favoriteCountryList.data!!) {
                    if(stringToCountry(savedRecipe.details).iD == args.result.iD){
                        changeMenuItemColor(menuItem, R.color.yellow)
                        savedCountryId = savedRecipe.id
                        countrySaved = true
                        break
                    }
                    else{
                        changeMenuItemColor(menuItem,R.color.white)
                    }
                }
            } catch (e: Exception) {
                Log.d("DetailsActivity", e.message.toString())
            }
        })
    }

    private fun saveToFavorites(item: MenuItem) {
        countryViewModel.insertFavCountry(args.result.country,args.result)
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("Added to favorites.")
        countrySaved = true
    }

    private fun removeFromFavorites(item: MenuItem) {
        countryViewModel.deleteFavCountry(savedCountryId)
        changeMenuItemColor(item, R.color.white)
        showSnackBar("Removed from Favorites.")
        countrySaved = false
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            detailsLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this, color))
    }
}


