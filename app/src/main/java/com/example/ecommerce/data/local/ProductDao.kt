package com.example.ecommerce.data.local

import androidx.room.*
import androidx.room.Dao
import com.example.ecommerce.data.model.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    suspend fun getAllProducts(): List<Product>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<Product>)

    @Query("SELECT COUNT(*) FROM products")
    suspend fun countProducts(): Int

}
