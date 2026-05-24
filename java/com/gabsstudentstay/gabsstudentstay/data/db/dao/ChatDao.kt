package com.gabsstudentstay.gabsstudentstay.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gabsstudentstay.gabsstudentstay.data.db.model.ChatMessage

@Dao
interface ChatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessage): Long

    // All messages in a conversation between two users about a specific listing
    @Query("""
        SELECT * FROM chat_messages
        WHERE listingId = :listingId
        AND (
            (senderId = :userId1 AND receiverId = :userId2)
            OR
            (senderId = :userId2 AND receiverId = :userId1)
        )
        ORDER BY timestamp ASC
    """)
    fun getConversation(listingId: Int, userId1: Int, userId2: Int): LiveData<List<ChatMessage>>

    // All unique conversations for a user (latest message per conversation)
    @Query("""
        SELECT * FROM chat_messages
        WHERE senderId = :userId OR receiverId = :userId
        GROUP BY listingId,
            CASE WHEN senderId = :userId THEN receiverId ELSE senderId END
        ORDER BY timestamp DESC
    """)
    fun getConversationList(userId: Int): LiveData<List<ChatMessage>>

    // Mark messages as read
    @Query("""
        UPDATE chat_messages SET isRead = 1
        WHERE receiverId = :userId AND listingId = :listingId AND isRead = 0
    """)
    suspend fun markMessagesAsRead(userId: Int, listingId: Int)

    // Count unread messages for a user
    @Query("SELECT COUNT(*) FROM chat_messages WHERE receiverId = :userId AND isRead = 0")
    fun getUnreadCount(userId: Int): LiveData<Int>
}