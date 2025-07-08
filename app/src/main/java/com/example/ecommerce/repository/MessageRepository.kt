package com.example.ecommerce.repository

import com.example.ecommerce.data.local.MessageDao
import com.example.ecommerce.data.model.Message
import kotlinx.coroutines.flow.Flow

class MessageRepository(private val dao: MessageDao) {
    fun getAllMessages(): Flow<List<Message>> = dao.getAllMessages()
    suspend fun insertMessage(message: Message) = dao.insertMessage(message)
}

