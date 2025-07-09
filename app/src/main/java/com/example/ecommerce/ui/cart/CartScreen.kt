package com.example.ecommerce.ui.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
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
import com.example.ecommerce.data.model.CartItem
import com.example.ecommerce.data.model.CartItemWithProduct
import com.example.ecommerce.util.ImageHelper
import com.example.ecommerce.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavHostController,
    cartViewModel: CartViewModel,
    onCheckout: () -> Unit
) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(true) {
        cartViewModel.loadCart()
    }

    val total = cartItems.sumOf { it.product.price * it.cartItem.quantity }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Giỏ hàng") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (cartItems.isEmpty()) {
                // 👉 Hiển thị khi giỏ hàng trống
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "🛒",
                        style = MaterialTheme.typography.displayLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Giỏ hàng của bạn đang trống",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Hãy thêm sản phẩm vào giỏ hàng",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                // 👉 Hiển thị danh sách sản phẩm trong giỏ hàng
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(cartItems) { item ->
                        CartItemCard(
                            item = item,
                            onQuantityChange = { newQuantity ->
                                cartViewModel.updateQuantity(item.cartItem.productId, newQuantity, context)
                            },
                            onRemove = {
                                cartViewModel.removeFromCart(item.cartItem.productId, context)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                // 👉 Tổng tiền và nút thanh toán
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Tổng cộng:",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$total USD",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                cartViewModel.clearCart()
                                onCheckout()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Thanh toán", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemCard(
    item: CartItemWithProduct,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    val product = item.product
    val quantity = item.cartItem.quantity
    val imageResId = ImageHelper.getProductImageResource(product.id)
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Dialog xác nhận xóa
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Xóa sản phẩm") },
            text = { Text("Bạn có muốn xóa \"${product.name}\" khỏi giỏ hàng?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onRemove()
                        showDeleteDialog = false
                    }
                ) {
                    Text("Xóa")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Hủy")
                }
            }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 📷 Ảnh sản phẩm
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = product.name,
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 12.dp),
                contentScale = ContentScale.Crop
            )

            // 📝 Thông tin sản phẩm
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${product.price} USD",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Tổng: ${product.price * quantity} USD",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                // 🔢 Quantity controls - Di chuyển vào giữa
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Nút giảm
                    IconButton(
                        onClick = {
                            if (quantity > 1) {
                                onQuantityChange(quantity - 1)
                            } else {
                                // Hiển thị dialog xác nhận xóa
                                showDeleteDialog = true
                            }
                        }
                    ) {
                        Icon(
                            Icons.Default.Remove,
                            contentDescription = "Giảm số lượng"
                        )
                    }

                    // Hiển thị số lượng
                    Text(
                        text = quantity.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    // Nút tăng
                    IconButton(
                        onClick = {
                            onQuantityChange(quantity + 1)
                        }
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Tăng số lượng"
                        )
                    }
                }
            }
        }
    }
}
