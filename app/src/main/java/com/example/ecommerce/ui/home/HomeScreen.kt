package com.example.ecommerce.ui.home

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.ecommerce.R
import com.example.ecommerce.data.model.Product
import com.example.ecommerce.viewmodel.CartViewModel
import com.example.ecommerce.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel
) {
    val products by productViewModel.products.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        productViewModel.loadProducts()
        cartViewModel.loadCart()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trang chủ") },
                actions = {
                    // Nút Logout
                    IconButton(onClick = {
                        // Xoá token đăng nhập (nếu có)
                        val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                        sharedPreferences.edit().clear().apply()
                        navController.navigate("signin") {
                            popUpTo("home") { inclusive = true }
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_logout),
                            contentDescription = "Đăng xuất"
                        )
                    }

                    // Nút Giỏ hàng
                    IconButton(onClick = {
                        navController.navigate("cart")
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cart),
                            contentDescription = "Giỏ hàng"
                        )
                    }
                }
            )
        },

        // 🔥 Nút Chat (Floating Action Button)
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("chat") }) {
                Icon(Icons.Default.Chat, contentDescription = "Chat")
            }
        }

    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp)
        ) {
            items(products) { product ->
                ProductItem(
                    product = product,
                    onClick = {
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("product", product)
                        navController.navigate("product_detail")
                    }
                )
            }
        }
    }
}



@Composable
fun ProductItem(product: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            Image(
                painter = rememberAsyncImagePainter(product.imageUrl),
                contentDescription = product.name,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(product.name, fontWeight = FontWeight.Bold)
                Text("${product.price} USD", color = MaterialTheme.colorScheme.primary)
                Text(product.description, maxLines = 2)
            }
        }
    }
}



