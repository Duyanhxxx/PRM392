package com.example.ecommerce.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ecommerce.data.model.CartItem

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    suspend fun getAllCartItems(): List<CartItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cartItem: CartItem)

    @Query("UPDATE cart_items SET quantity = :newQuantity WHERE productId = :productId")
    suspend fun updateQuantity(productId: Int, newQuantity: Int)

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun deleteByProductId(productId: Int)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}
