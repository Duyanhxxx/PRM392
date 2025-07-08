package com.example.ecommerce.data.local

import androidx.room.*
import com.example.ecommerce.data.model.CartItem

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    suspend fun getAllCartItems(): List<CartItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cartItem: CartItem)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}
