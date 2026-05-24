package com.gabsstudentstay.gabsstudentstay.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.gabsstudentstay.gabsstudentstay.data.db.AppDatabase
import com.gabsstudentstay.gabsstudentstay.data.db.model.Listing
import com.gabsstudentstay.gabsstudentstay.data.db.repository.ListingRepository
import kotlinx.coroutines.launch

class ListingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ListingRepository(
        AppDatabase.getInstance(application).listingDao()
    )

    val listings: LiveData<List<Listing>> = repository.getAvailableListings()

    private val _filteredListings = MediatorLiveData<List<Listing>>().apply {
        addSource(listings) { value ->
            if (_isFiltered.value == false) {
                this.value = value
            }
        }
    }
    val filteredListings: LiveData<List<Listing>> = _filteredListings

    private val _isFiltered = MutableLiveData(false)
    val isFiltered: LiveData<Boolean> = _isFiltered

    private val _selectedListing = MutableLiveData<Listing?>()
    val selectedListing: LiveData<Listing?> = _selectedListing

    fun applyFilter(
        minPrice: Double = 0.0,
        maxPrice: Double = 10000.0,
        location: String = "",
        availableFrom: String = "",
        type: String = ""
    ) {
        repository.filterListings(minPrice, maxPrice, location, availableFrom, type)
            .observeForever { results ->
                _filteredListings.value = results
            }
        _isFiltered.value = true
    }

    fun clearFilter() {
        _isFiltered.value = false
        _filteredListings.value = listings.value ?: emptyList()
    }

    fun search(query: String) {
        if (query.isBlank()) {
            clearFilter()
            return
        }
        repository.searchListings(query).observeForever { results ->
            _filteredListings.value = results
        }
        _isFiltered.value = true
    }

    fun selectListing(listing: Listing) {
        _selectedListing.value = listing
    }

    fun loadListingById(id: Int) {
        viewModelScope.launch {
            _selectedListing.value = repository.getListingById(id)
        }
    }
}