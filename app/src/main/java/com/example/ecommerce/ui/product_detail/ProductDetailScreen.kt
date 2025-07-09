package com.example.ecommerce.ui.product_detail

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ecommerce.R
import com.example.ecommerce.data.model.Product
import com.example.ecommerce.data.model.CartItem
import com.example.ecommerce.viewmodel.CartViewModel
import com.example.ecommerce.util.ImageHelper

@Composable
fun ProductDetailScreen(
    product: Product,
    cartViewModel: CartViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Sử dụng ImageHelper
        val imageResId = ImageHelper.getProductImageResource(product.id)
        
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = product.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Crop
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        Text(product.name, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text("${product.price} USD", color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))
        Text(product.description)

        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = {
                cartViewModel.addToCart(
                    item = CartItem(productId = product.id, quantity = 1),
                    context = context
                )
                // Hiển thị toast thành công
                Toast.makeText(context, "Thêm ${product.name} vào giỏ hàng thành công! ✅", Toast.LENGTH_SHORT).show()
                onBack()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Thêm vào giỏ hàng")
        }
    }
}
