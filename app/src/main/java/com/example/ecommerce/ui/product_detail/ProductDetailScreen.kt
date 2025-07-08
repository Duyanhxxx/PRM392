package com.example.ecommerce.ui.product_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.ecommerce.data.model.Product
import com.example.ecommerce.data.model.CartItem
import com.example.ecommerce.viewmodel.CartViewModel

@Composable
fun ProductDetailScreen(
    product: Product,
    cartViewModel: CartViewModel,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(product.imageUrl),
            contentDescription = product.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(product.name, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text("${product.price} USD", color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))
        Text(product.description)

        Spacer(modifier = Modifier.height(24.dp))
        val context = LocalContext.current
        Button(
            onClick = {
                cartViewModel.addToCart(
                    item = CartItem(productId = product.id, quantity = 1),
                    context = context
                )
                onBack()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Thêm vào giỏ hàng")
        }
    }
}
