package com.gabsstudentstay.gabsstudentstay.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true)
    val messageId: Int = 0,
    val senderId: Int,
    val receiverId: Int,
    val listingId: Int,
    val message: String,
    val isRead: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
