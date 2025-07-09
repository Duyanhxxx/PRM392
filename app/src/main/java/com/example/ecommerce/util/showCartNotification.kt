package com.example.ecommerce.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.ecommerce.R

fun showCartNotification(context: Context) {
    val channelId = "cart_channel"
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // Tạo channel nếu Android >= O
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "Cart Notification",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_cart)
        .setContentTitle("Bạn có sản phẩm trong giỏ hàng")
        .setContentText("Đừng quên thanh toán nhé!")
        .setAutoCancel(true)
        .build()

    notificationManager.notify(1, notification)
}

fun showAddToCartSuccessNotification(context: Context, productName: String) {
    val channelId = "cart_success_channel"
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // Tạo channel nếu Android >= O
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "Add to Cart Success",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_cart)
        .setContentTitle("Thêm vào giỏ hàng thành công! ✅")
        .setContentText("$productName đã được thêm vào giỏ hàng")
        .setAutoCancel(true)
        .build()

    notificationManager.notify(2, notification)
}
