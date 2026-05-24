package com.gabsstudentstay.gabsstudentstay.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "preferences")
data class Preference(
    @PrimaryKey(autoGenerate = true)
    val prefId: Int = 0,
    val userId: Int,                    // FK → User.userId (one per user)
    val minPrice: Double = 0.0,
    val maxPrice: Double = 5000.0,
    val location: String = "",          // Empty = any location
    val availableFrom: String = "",     // ISO date or empty = any date
    val type: String = ""               // Empty = any type
)