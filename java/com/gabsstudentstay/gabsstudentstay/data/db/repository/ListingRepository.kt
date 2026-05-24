package com.gabsstudentstay.gabsstudentstay.data.db.repository

import androidx.lifecycle.LiveData
import com.gabsstudentstay.gabsstudentstay.data.db.dao.ListingDao
import com.gabsstudentstay.gabsstudentstay.data.db.model.Listing

class ListingRepository(private val listingDao: ListingDao) {

    fun getAvailableListings(): LiveData<List<Listing>> =
        listingDao.getAvailableListings()

    fun getAllListings(): LiveData<List<Listing>> =
        listingDao.getAllListings()

    fun filterListings(
        minPrice: Double, maxPrice: Double,
        location: String, availableFrom: String, type: String
    ): LiveData<List<Listing>> =
        listingDao.filterListings(minPrice, maxPrice, location, availableFrom, type)

    fun searchListings(query: String): LiveData<List<Listing>> =
        listingDao.searchListings(query)

    suspend fun getListingById(id: Int): Listing? =
        listingDao.getListingById(id)

    suspend fun insertListing(listing: Listing): Long =
        listingDao.insertListing(listing)

    suspend fun markAsReserved(listingId: Int) =
        listingDao.markAsReserved(listingId)

    suspend fun getListingCount(): Int =
        listingDao.getListingCount()

    suspend fun insertAll(listings: List<Listing>) =
        listingDao.insertAll(listings)
}