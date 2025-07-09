package com.example.ecommerce.repository

import com.example.ecommerce.data.local.CartDao
import com.example.ecommerce.data.local.ProductDao
import com.example.ecommerce.data.model.CartItem
import com.example.ecommerce.data.model.CartItemWithProduct

class CartRepository(
    private val cartDao: CartDao,
    private val productDao: ProductDao
) {
    suspend fun getCartItemsWithProducts(): List<CartItemWithProduct> {
        val cartItems = cartDao.getAllCartItems()
        val products = productDao.getAllProducts()
        return cartItems.mapNotNull { item ->
            val product = products.find { it.id == item.productId }
            product?.let {
                CartItemWithProduct(item, it)
            }
        }
    }

    suspend fun insertCartItem(item: CartItem) = cartDao.insert(item)
    suspend fun updateCartItemQuantity(productId: Int, newQuantity: Int) = cartDao.updateQuantity(productId, newQuantity)
    suspend fun removeCartItem(productId: Int) = cartDao.deleteByProductId(productId)
    suspend fun clearCart() = cartDao.clearCart()
}
