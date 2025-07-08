package com.example.ecommerce.ui.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ecommerce.viewmodel.CartViewModel

@Composable
fun CartScreen(
    navController: NavHostController,
    cartViewModel: CartViewModel,
    onCheckout: () -> Unit
) {
    val cartItems by cartViewModel.cartItems.collectAsState()

    LaunchedEffect(true) {
        cartViewModel.loadCart()
    }

    val total = cartItems.sumOf { it.product.price * it.cartItem.quantity }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Giỏ hàng", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        if (cartItems.isEmpty()) {
            // 👉 Hiển thị khi giỏ hàng trống
            Text(
                text = "Giỏ hàng của bạn đang trống.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(24.dp)
            )
        } else {
            // 👉 Hiển thị danh sách sản phẩm
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cartItems) { item ->
                    val product = item.product
                    val qty = item.cartItem.quantity
                    Text("${product.name} x$qty - ${product.price * qty} USD")
                }
            }

            val total = cartItems.sumOf { it.product.price * it.cartItem.quantity }

            Text("Tổng: $total USD", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    cartViewModel.clearCart()
                    navController.navigate("checkout")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Thanh toán")
            }
        }
    }
}


