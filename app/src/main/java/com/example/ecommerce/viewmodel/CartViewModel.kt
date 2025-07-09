package com.example.ecommerce.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.model.CartItem
import com.example.ecommerce.data.model.CartItemWithProduct
import com.example.ecommerce.repository.CartRepository
import com.example.ecommerce.util.showCartNotification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel(private val repo: CartRepository) : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItemWithProduct>>(emptyList())
    val cartItems: StateFlow<List<CartItemWithProduct>> = _cartItems

    fun loadCart() {
        viewModelScope.launch {
            _cartItems.value = repo.getCartItemsWithProducts()
        }
    }

    fun addToCart(item: CartItem, context: Context) {
        viewModelScope.launch {
            repo.insertCartItem(item)
            showCartNotification(context)
            loadCart()
        }
    }

    fun updateQuantity(productId: Int, newQuantity: Int, context: Context) {
        viewModelScope.launch {
            if (newQuantity > 0) {
                repo.updateCartItemQuantity(productId, newQuantity)
                showCartNotification(context)
                loadCart()
            }
        }
    }

    fun removeFromCart(productId: Int, context: Context) {
        viewModelScope.launch {
            repo.removeCartItem(productId)
            showCartNotification(context)
            loadCart()
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repo.clearCart()
            loadCart()
        }
    }

    suspend fun getCartItemsWithProduct(): List<CartItemWithProduct> {
        return repo.getCartItemsWithProducts()
    }
}
