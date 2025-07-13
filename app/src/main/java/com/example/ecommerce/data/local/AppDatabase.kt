package com.example.ecommerce.data.local

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.ecommerce.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [User::class, Product::class, CartItem::class, Message::class],
    version = 4, // 🔺 Tăng version để reset DB nếu cần
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun messageDao(): MessageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ecommerce_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { db ->
                        INSTANCE = db

                        scope.launch {
                            val productDao = db.productDao()

                            // ✅ Dùng count thay vì isEmpty
                            val count = productDao.countProducts()
                            if (count < 15) {
                                productDao.insertAll(preloadProducts())
                            }
                        }
                    }
            }
        }

        private class DatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        val productDao = database.productDao()
                        productDao.insertAll(preloadProducts())
                    }
                }
            }
        }

        private fun preloadProducts(): List<Product> {
            return listOf(
                Product(1, "MacBook Pro M3", "Apple 14-inch MacBook Pro M3", 2199.0, "product1"),
                Product(2, "Samsung Galaxy Watch 6", "Smartwatch AMOLED, đo nhịp tim", 299.0, "product2"),
                Product(3, "Tai nghe Sony WH-1000XM5", "Chống ồn chủ động, pin 30h", 399.0, "product3"),
                Product(4, "Chuột Logitech MX Master 3S", "Ergonomic mouse, sạc USB-C", 129.0, "product4"),
                Product(5, "Bàn phím Keychron K6", "Bluetooth, RGB, Switch Red", 109.0, "product5"),
                Product(6, "iPhone 15 Pro Max", "A17 Pro, màn hình 6.7 inch OLED", 1299.0, "product6"),
                Product(7, "iPad Air 5 M1", "10.9-inch Liquid Retina, chip M1", 799.0, "product7"),
                Product(8, "Samsung Galaxy Z Fold5", "Gập màn hình, Snapdragon 8 Gen 2", 1799.0, "product8"),
                Product(9, "Ổ cứng SSD Samsung T7", "Portable SSD 1TB USB 3.2", 139.0, "product9"),
                Product(10, "Màn hình LG UltraGear 27\"", "Gaming 2K 165Hz IPS", 349.0, "product10"),
                Product(11, "Loa Bluetooth JBL Flip 6", "Chống nước IP67, pin 12h", 119.0, "product11"),
                Product(12, "Camera Xiaomi Mi Home 360°", "Giám sát Full HD, xoay 360 độ", 49.0, "product12"),
                Product(13, "Máy lọc không khí Xiaomi 4 Lite", "HEPA lọc bụi mịn PM2.5", 149.0, "product13"),
                Product(14, "Pin sạc dự phòng Anker 20.000mAh", "Power Delivery, sạc nhanh", 69.0, "product14"),
                Product(15, "Đèn bàn Xiaomi Yeelight", "LED, điều chỉnh độ sáng, màu", 45.0, "product15")
            )
        }
    }
}

