package com.example.ecommerce.repository

import com.example.ecommerce.data.local.ProductDao
import com.example.ecommerce.data.model.Product

class ProductRepository(private val dao: ProductDao) {
    suspend fun getAllProducts(): List<Product> = dao.getAllProducts()
}
