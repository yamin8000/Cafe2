package io.github.yamin8000.cafe.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.yamin8000.cafe.db.entities.account.Account
import io.github.yamin8000.cafe.db.entities.account.AccountDao
import io.github.yamin8000.cafe.db.entities.category.Category
import io.github.yamin8000.cafe.db.entities.category.CategoryDao
import io.github.yamin8000.cafe.db.entities.day.Day
import io.github.yamin8000.cafe.db.entities.day.DayDao
import io.github.yamin8000.cafe.db.entities.order.*
import io.github.yamin8000.cafe.db.entities.product.Product
import io.github.yamin8000.cafe.db.entities.product.ProductDao
import io.github.yamin8000.cafe.db.entities.relatives.RelativeDao
import io.github.yamin8000.cafe.db.entities.subscriber.Subscriber
import io.github.yamin8000.cafe.db.entities.subscriber.SubscriberDao

@Database(
    entities = [
        Order::class,
        OrderDetail::class,
        Product::class,
        Day::class,
        Category::class,
        Subscriber::class,
        Account::class,
    ],
    version = 1
)
@TypeConverters(DateConverter::class, OrderDetailConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun orderDetailDao(): OrderDetailDao
    abstract fun dayDao(): DayDao
    abstract fun productDao(): ProductDao
    abstract fun categoryDao(): CategoryDao
    abstract fun relativeDao(): RelativeDao
    abstract fun subscriberDao(): SubscriberDao
    abstract fun accountDao(): AccountDao
}
