package com.example.ecommerce.data.local

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.ecommerce.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [User::class, Product::class, CartItem::class, Message::class],
    version = 3,
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

                        // ✅ Preload nếu danh sách sản phẩm đang rỗng
                        scope.launch {
                            val productDao = db.productDao()
                            if (productDao.getAllProducts().isEmpty()) {
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
                Product(1, "MacBook Pro M3", "Apple 14-inch MacBook Pro M3", 2199.0,
                    "https://product.hstatic.net/200000348419/product/macbook_pro_14_inch_m3-pro-black_5a67a95d906d49a79bf0ff37c4f9a175_master.png"),
                Product(2, "Samsung Galaxy Watch 6", "Smartwatch AMOLED, đo nhịp tim", 299.0,
                    "https://cdn.tgdd.vn/Products/Images/7077/311042/samsung-galaxy-watch-6-44mm-vang-kem-tn-1-600x600.jpg"),
                Product(3, "Tai nghe Sony WH-1000XM5", "Chống ồn chủ động, pin 30h", 399.0,
                    "https://cdn.tgdd.vn/Products/Images/54/301650/tai-nghe-bluetooth-sony-wh-1000xm5-thumb-600x600.jpg"),
                Product(4, "Chuột Logitech MX Master 3S", "Ergonomic mouse, sạc USB-C", 129.0,
                    "https://cdn.tgdd.vn/Products/Images/86/309307/logitech-mx-master-3s-den-thumb-600x600.jpg"),
                Product(5, "Bàn phím Keychron K6", "Bluetooth, RGB, Switch Red", 109.0,
                    "https://cdn.tgdd.vn/Products/Images/86/289855/keychron-k6-wireless-thumb-600x600.jpg") ,
                        Product(6, "iPhone 15 Pro Max", "A17 Pro, màn hình 6.7 inch OLED", 1299.0,
                "https://cdn.tgdd.vn/Products/Images/42/305659/iphone-15-pro-max-blue-thumbnew-600x600.jpg"),
            Product(7, "iPad Air 5 M1", "10.9-inch Liquid Retina, chip M1", 799.0,
                "https://cdn.tgdd.vn/Products/Images/522/274199/iPad-air-5-xanh-thumb-600x600.jpg"),
            Product(8, "Samsung Galaxy Z Fold5", "Gập màn hình, Snapdragon 8 Gen 2", 1799.0,
                "https://cdn.tgdd.vn/Products/Images/42/301648/samsung-galaxy-z-fold5-xanh-duong-1-600x600.jpg"),
            Product(9, "Ổ cứng SSD Samsung T7", "Portable SSD 1TB USB 3.2", 139.0,
                "https://cdn.tgdd.vn/Products/Images/7078/230728/samsung-portable-ssd-t7-thumb-600x600.jpg"),
            Product(10, "Màn hình LG UltraGear 27\"", "Gaming 2K 165Hz IPS", 349.0,
                "https://cdn.tgdd.vn/Products/Images/5698/309316/lg-ultragear-27gr75q-b-27inch-thumb-600x600.jpg"),
            Product(11, "Loa Bluetooth JBL Flip 6", "Chống nước IP67, pin 12h", 119.0,
                "https://cdn.tgdd.vn/Products/Images/2162/290548/jbl-flip-6-den-thumb-600x600.jpg"),
            Product(12, "Camera Xiaomi Mi Home 360°", "Giám sát Full HD, xoay 360 độ", 49.0,
                "https://cdn.tgdd.vn/Products/Images/4727/232275/xiaomi-mi-home-camera-360-1080p-thumb-600x600.jpg"),
            Product(13, "Máy lọc không khí Xiaomi 4 Lite", "HEPA lọc bụi mịn PM2.5", 149.0,
                "https://cdn.tgdd.vn/Products/Images/5473/307551/may-loc-khong-khi-xiaomi-smart-air-purifier-4-lite-trang-thumb-600x600.jpg"),
            Product(14, "Pin sạc dự phòng Anker 20.000mAh", "Power Delivery, sạc nhanh", 69.0,
                "https://cdn.tgdd.vn/Products/Images/57/248206/anker-powercore-select-20000mah-den-thumb-600x600.jpg"),
            Product(15, "Đèn bàn Xiaomi Yeelight", "LED, điều chỉnh độ sáng, màu", 45.0,
                "https://cdn.tgdd.vn/Products/Images/2162/267803/yeelight-led-desk-lamp-v1-pro-thumb-600x600.jpg")

            )
        }
    }
}


