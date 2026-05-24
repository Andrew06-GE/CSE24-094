package com.gabsstudentstay.gabsstudentstay.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.gabsstudentstay.gabsstudentstay.data.db.AppDatabase
import com.gabsstudentstay.gabsstudentstay.data.db.model.ChatMessage
import com.gabsstudentstay.gabsstudentstay.data.db.repository.ChatRepository
import kotlinx.coroutines.launch

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ChatRepository(
        AppDatabase.getInstance(application).chatDao()
    )

    fun getConversation(listingId: Int, userId1: Int, userId2: Int): LiveData<List<ChatMessage>> =
        repository.getConversation(listingId, userId1, userId2)

    fun getConversationList(userId: Int): LiveData<List<ChatMessage>> =
        repository.getConversationList(userId)

    fun getUnreadCount(userId: Int): LiveData<Int> =
        repository.getUnreadCount(userId)

    fun sendMessage(senderId: Int, receiverId: Int, listingId: Int, text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            val message = ChatMessage(
                senderId   = senderId,
                receiverId = receiverId,
                listingId  = listingId,
                message    = text.trim()
            )
            repository.sendMessage(message)
        }
    }

    fun markRead(userId: Int, listingId: Int) {
        viewModelScope.launch {
            repository.markMessagesAsRead(userId, listingId)
        }
    }
}