package com.gabsstudentstay.gabsstudentstay.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservations")
data class Reservation(
    @PrimaryKey(autoGenerate = true)
    val reservationId: Int = 0,
    val userId: Int,                    // FK → User.userId
    val listingId: Int,                 // FK → Listing.listingId
    val referenceNumber: String,        // e.g. "GSS-2026-00042"
    val depositPaid: Double,
    val status: String = "pending",     // "pending", "confirmed", "cancelled"
    val timestamp: Long = System.currentTimeMillis()
)
