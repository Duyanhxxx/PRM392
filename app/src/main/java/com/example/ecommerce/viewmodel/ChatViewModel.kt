package com.example.ecommerce.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.model.Message
import com.example.ecommerce.repository.MessageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(private val repo: MessageRepository) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()

    init {
        loadMessages()
    }

    fun loadMessages() {
        viewModelScope.launch {
            repo.getAllMessages().collect {
                _messages.value = it
            }
        }
    }

    fun sendMessage(text: String) {
        viewModelScope.launch {
            repo.insertMessage(Message(text = text, isFromUser = true))
        }
    }
}


