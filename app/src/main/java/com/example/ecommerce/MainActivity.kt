package com.example.ecommerce

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.ecommerce.data.local.AppDatabase
import com.example.ecommerce.repository.*
import com.example.ecommerce.navigation.AppNavigation
import com.example.ecommerce.ui.theme.EcommerceTheme
import com.example.ecommerce.util.showCartNotification
import com.example.ecommerce.viewmodel.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Khởi tạo Room database
        val db = AppDatabase.getDatabase(this, scope)

        // ✅ Tạo repository
        val productRepository = ProductRepository(db.productDao())
        val cartRepository = CartRepository(db.cartDao(), db.productDao())
        val messageRepository = MessageRepository(db.messageDao())

        // ✅ Tạo ViewModel thủ công (không dùng viewModel {} ở đây)
        val productViewModel = ProductViewModel(productRepository)
        val cartViewModel = CartViewModel(cartRepository)
        val chatViewModel = ChatViewModel(messageRepository)

        // ✅ Hiển thị notification nếu giỏ hàng có sản phẩm
        lifecycleScope.launch {
            val items = cartViewModel.getCartItemsWithProduct()
            if (items.isNotEmpty()) {
                showCartNotification(this@MainActivity)
            }
        }

        // ✅ Set UI
        setContent {
            EcommerceTheme {
                val navController = rememberNavController()

                // Chỉ dùng viewModel {} cho ViewModel có constructor mặc định hoặc từ AndroidViewModelFactory
                val authViewModel = androidx.lifecycle.viewmodel.compose.viewModel {
                    AuthViewModel(application)
                }

                AppNavigation(
                    navController = navController,
                    productViewModel = productViewModel,
                    cartViewModel = cartViewModel,
                    authViewModel = authViewModel,
                    chatViewModel = chatViewModel
                )
            }
        }
    }
}







