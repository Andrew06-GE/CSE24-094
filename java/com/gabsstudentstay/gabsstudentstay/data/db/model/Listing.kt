package com.gabsstudentstay.gabsstudentstay.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "listings")
data class Listing(
    @PrimaryKey(autoGenerate = true)
    val listingId: Int = 0,
    val providerId: Int,
    val title: String,
    val price: Double,
    val location: String,
    val type: String,
    val amenities: String,
    val availabilityDate: String,
    val depositAmount: Double,
    val imageUri: String = "",
    val description: String = "",
    val latitude: Double = -24.6541,
    val longitude: Double = 25.9087,
    val isReserved: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)