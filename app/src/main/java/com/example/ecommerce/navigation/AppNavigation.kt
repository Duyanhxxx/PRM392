package com.example.ecommerce.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ecommerce.data.model.Product
import com.example.ecommerce.ui.auth.SignInScreen
import com.example.ecommerce.ui.auth.SignUpScreen
import com.example.ecommerce.ui.cart.CartScreen
import com.example.ecommerce.ui.chat.ChatScreen
import com.example.ecommerce.ui.checkout.CheckoutScreen
import com.example.ecommerce.ui.splash.SplashScreen
import com.example.ecommerce.ui.home.HomeScreen
import com.example.ecommerce.ui.product_detail.ProductDetailScreen
import com.example.ecommerce.viewmodel.AuthViewModel
import com.example.ecommerce.viewmodel.CartViewModel
import com.example.ecommerce.viewmodel.ProductViewModel
import com.example.ecommerce.viewmodel.ChatViewModel


@Composable
fun AppNavigation(
    navController: NavHostController,
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    authViewModel: AuthViewModel,
    chatViewModel: ChatViewModel
) {
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("signin") {
            SignInScreen(navController, authViewModel)
        }
        composable("signup") {
            SignUpScreen(navController, authViewModel)
        }
        composable("home") {
            HomeScreen(navController, productViewModel, cartViewModel)
        }
        composable("product_detail") {
            val product = navController.previousBackStackEntry
                ?.savedStateHandle?.get<Product>("product")

            product?.let {
                ProductDetailScreen(
                    product = it,
                    cartViewModel = cartViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
        composable("cart") {
            CartScreen(
                navController = navController,
                cartViewModel = cartViewModel,
                onCheckout = {
                    navController.navigate("checkout")
                }
            )
        }


        composable("checkout") {
            CheckoutScreen {
                navController.navigate("home") {
                    popUpTo("checkout") { inclusive = true }
                }
            }
        }
        composable("chat") {
            ChatScreen(chatViewModel = chatViewModel)
        }
    }
}


