package com.example.ecommerce.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.local.AppDatabase
import com.example.ecommerce.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao = AppDatabase.getDatabase(application, viewModelScope).userDao()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> get() = _isLoggedIn

    fun register(name: String, email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val existing = userDao.getUserByEmail(email)
            if (existing == null) {
                userDao.insertUser(User(name = name, email = email, password = password))
                onSuccess()
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val user = userDao.login(email, password)
            _isLoggedIn.value = user != null
        }
    }

    fun resetLoginState() {
        _isLoggedIn.value = false
    }
}


