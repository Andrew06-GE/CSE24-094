package com.gabsstudentstay.gabsstudentstay.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gabsstudentstay.gabsstudentstay.data.db.model.Listing

@Dao
interface ListingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListing(listing: Listing): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(listings: List<Listing>)

    @Update
    suspend fun updateListing(listing: Listing)

    @Delete
    suspend fun deleteListing(listing: Listing)

    // All available (not reserved) listings
    @Query("SELECT * FROM listings WHERE isReserved = 0 ORDER BY createdAt DESC")
    fun getAvailableListings(): LiveData<List<Listing>>

    // All listings (for providers/admin)
    @Query("SELECT * FROM listings ORDER BY createdAt DESC")
    fun getAllListings(): LiveData<List<Listing>>

    // Single listing by ID
    @Query("SELECT * FROM listings WHERE listingId = :id LIMIT 1")
    suspend fun getListingById(id: Int): Listing?

    // Listings by a specific provider
    @Query("SELECT * FROM listings WHERE providerId = :providerId")
    fun getListingsByProvider(providerId: Int): LiveData<List<Listing>>

    // Smart filter query — all parameters optional (empty string = match all)
    @Query("""
        SELECT * FROM listings
        WHERE isReserved = 0
        AND price BETWEEN :minPrice AND :maxPrice
        AND (:location = '' OR location LIKE '%' || :location || '%')
        AND (:availableFrom = '' OR availabilityDate <= :availableFrom)
        AND (:type = '' OR type = :type)
        ORDER BY price ASC
    """)
    fun filterListings(
        minPrice: Double,
        maxPrice: Double,
        location: String,
        availableFrom: String,
        type: String
    ): LiveData<List<Listing>>

    // Mark a listing as reserved (called after successful deposit)
    @Query("UPDATE listings SET isReserved = 1 WHERE listingId = :listingId")
    suspend fun markAsReserved(listingId: Int)

    // Suspend version — used by WorkManager (cannot observe LiveData off main thread)
    @Query("""
        SELECT * FROM listings
        WHERE isReserved = 0
        AND price BETWEEN :minPrice AND :maxPrice
        AND (:location = '' OR location LIKE '%' || :location || '%')
        AND (:availableFrom = '' OR availabilityDate <= :availableFrom)
        AND (:type = '' OR type = :type)
        ORDER BY price ASC
    """)
    suspend fun filterListingsSync(
        minPrice: Double, maxPrice: Double,
        location: String, availableFrom: String, type: String
    ): List<Listing>

    // Search by title or location keyword
    @Query("""
        SELECT * FROM listings
        WHERE isReserved = 0
        AND (title LIKE '%' || :query || '%' OR location LIKE '%' || :query || '%')
    """)
    fun searchListings(query: String): LiveData<List<Listing>>

    @Query("SELECT COUNT(*) FROM listings")
    suspend fun getListingCount(): Int
}