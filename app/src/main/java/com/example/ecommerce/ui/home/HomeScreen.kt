package com.example.ecommerce.ui.home

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ecommerce.R
import com.example.ecommerce.data.model.Product
import com.example.ecommerce.viewmodel.CartViewModel
import com.example.ecommerce.viewmodel.ProductViewModel
import com.example.ecommerce.util.ImageHelper

// Enum cho các loại sort
enum class SortType {
    DEFAULT,
    PRICE_LOW_TO_HIGH,
    PRICE_HIGH_TO_LOW,
    NAME_A_TO_Z,
    NAME_Z_TO_A
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel
) {
    val products by productViewModel.products.collectAsState()
    val context = LocalContext.current
    var currentSortType by remember { mutableStateOf(SortType.DEFAULT) }
    var showSortMenu by remember { mutableStateOf(false) }
    
    // Sort products theo loại đã chọn
    val sortedProducts = remember(products, currentSortType) {
        when (currentSortType) {
            SortType.DEFAULT -> products
            SortType.PRICE_LOW_TO_HIGH -> products.sortedBy { it.price }
            SortType.PRICE_HIGH_TO_LOW -> products.sortedByDescending { it.price }
            SortType.NAME_A_TO_Z -> products.sortedBy { it.name }
            SortType.NAME_Z_TO_A -> products.sortedByDescending { it.name }
        }
    }

    LaunchedEffect(Unit) {
        productViewModel.loadProducts()
        cartViewModel.loadCart()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trang chủ") },
                actions = {
                    // Nút Sort với dropdown menu
                    Box {
                        IconButton(onClick = { showSortMenu = true }) {
                            Icon(
                                Icons.Default.Sort,
                                contentDescription = "Sắp xếp"
                            )
                        }
                        
                        // Dropdown menu nằm dưới icon Sort
                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false }
                        ) {
                            SortMenuItem(
                                text = "Mặc định",
                                isSelected = currentSortType == SortType.DEFAULT,
                                onClick = {
                                    currentSortType = SortType.DEFAULT
                                    showSortMenu = false
                                }
                            )
                            SortMenuItem(
                                text = "Giá: Thấp đến cao",
                                isSelected = currentSortType == SortType.PRICE_LOW_TO_HIGH,
                                onClick = {
                                    currentSortType = SortType.PRICE_LOW_TO_HIGH
                                    showSortMenu = false
                                }
                            )
                            SortMenuItem(
                                text = "Giá: Cao đến thấp",
                                isSelected = currentSortType == SortType.PRICE_HIGH_TO_LOW,
                                onClick = {
                                    currentSortType = SortType.PRICE_HIGH_TO_LOW
                                    showSortMenu = false
                                }
                            )
                            SortMenuItem(
                                text = "Tên: A-Z",
                                isSelected = currentSortType == SortType.NAME_A_TO_Z,
                                onClick = {
                                    currentSortType = SortType.NAME_A_TO_Z
                                    showSortMenu = false
                                }
                            )
                            SortMenuItem(
                                text = "Tên: Z-A",
                                isSelected = currentSortType == SortType.NAME_Z_TO_A,
                                onClick = {
                                    currentSortType = SortType.NAME_Z_TO_A
                                    showSortMenu = false
                                }
                            )
                        }
                    }
                    
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
        Column {
            // Hiển thị loại sort hiện tại
            if (currentSortType != SortType.DEFAULT) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = "Sắp xếp theo: ${getSortDisplayName(currentSortType)}",
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(12.dp)
            ) {
                items(sortedProducts) { product ->
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
}

// Helper function để hiển thị tên sort
fun getSortDisplayName(sortType: SortType): String {
    return when (sortType) {
        SortType.DEFAULT -> "Mặc định"
        SortType.PRICE_LOW_TO_HIGH -> "Giá thấp đến cao"
        SortType.PRICE_HIGH_TO_LOW -> "Giá cao đến thấp"
        SortType.NAME_A_TO_Z -> "Tên A-Z"
        SortType.NAME_Z_TO_A -> "Tên Z-A"
    }
}

// Component cho sort menu item
@Composable
fun SortMenuItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
                if (isSelected) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "✓",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        onClick = onClick
    )
}

@Composable
fun ProductItem(product: Product, onClick: () -> Unit) {
    val context = LocalContext.current
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            // Sử dụng ImageHelper
            val imageResId = ImageHelper.getProductImageResource(product.id)
            
            Image(
                painter = painterResource(id = imageResId),
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
