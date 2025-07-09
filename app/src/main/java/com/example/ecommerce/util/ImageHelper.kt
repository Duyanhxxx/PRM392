package com.example.ecommerce.util

import com.example.ecommerce.R

object ImageHelper {
    fun getProductImageResource(productId: Int): Int {
        return when(productId) {
            1 -> R.drawable.product1
            2 -> R.drawable.product2
            3 -> R.drawable.product3
            4 -> R.drawable.product4
            5 -> R.drawable.product5
            6 -> R.drawable.product6
            7 -> R.drawable.product7
            8 -> R.drawable.product8
            9 -> R.drawable.product9
            10 -> R.drawable.product10
            11 -> R.drawable.product11
            12 -> R.drawable.product12
            13 -> R.drawable.product13
            14 -> R.drawable.product14
            15 -> R.drawable.product15
            else -> R.drawable.ic_launcher_foreground
        }
    }
}
