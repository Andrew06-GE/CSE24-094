package com.gabsstudentstay.gabsstudentstay.data.db.repository

import androidx.lifecycle.LiveData
import com.gabsstudentstay.gabsstudentstay.data.db.dao.ChatDao
import com.gabsstudentstay.gabsstudentstay.data.db.model.ChatMessage

class ChatRepository(private val chatDao: ChatDao) {

    suspend fun sendMessage(message: ChatMessage): Long =
        chatDao.insertMessage(message)

    fun getConversation(listingId: Int, userId1: Int, userId2: Int): LiveData<List<ChatMessage>> =
        chatDao.getConversation(listingId, userId1, userId2)

    fun getConversationList(userId: Int): LiveData<List<ChatMessage>> =
        chatDao.getConversationList(userId)

    fun getUnreadCount(userId: Int): LiveData<Int> =
        chatDao.getUnreadCount(userId)

    suspend fun markMessagesAsRead(userId: Int, listingId: Int) =
        chatDao.markMessagesAsRead(userId, listingId)
}
