package io.github.yamin8000.cafe.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.yamin8000.cafe.db.order.Order
import io.github.yamin8000.cafe.db.order.OrderDao
import io.github.yamin8000.cafe.db.order.OrderDetailConverter
import io.github.yamin8000.cafe.db.product.Product
import io.github.yamin8000.cafe.db.product.ProductDao

@Database(entities = [Order::class, Product::class], version = 1)
@TypeConverters(DateConverter::class, OrderDetailConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun productDao(): ProductDao
}
