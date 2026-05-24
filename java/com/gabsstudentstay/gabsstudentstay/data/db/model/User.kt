package com.gabsstudentstay.gabsstudentstay.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0,
    val name: String,
    val email: String,
    val password: String,               // In production: store hashed password
    val role: String = "student",       // "student" or "provider"
    val studentId: String = "",
    val phone: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
