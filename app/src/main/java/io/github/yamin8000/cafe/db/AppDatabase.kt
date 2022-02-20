package io.github.yamin8000.cafe.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.yamin8000.cafe.db.order.*
import io.github.yamin8000.cafe.db.product.Product
import io.github.yamin8000.cafe.db.product.ProductDao

@Database(
    entities = [Order::class, OrderDetail::class, Product::class, Day::class],
    version = 1
)
@TypeConverters(DateConverter::class, OrderDetailConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun orderDetailDao(): OrderDetailDao
    abstract fun dayDao(): DayDao
    abstract fun productDao(): ProductDao
}
