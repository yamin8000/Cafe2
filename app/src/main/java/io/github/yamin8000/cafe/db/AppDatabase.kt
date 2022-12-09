/*
 *     Cafe/Cafe.app.main
 *     AppDatabase.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     AppDatabase.kt Last modified at 2022/12/9
 *     This file is part of Cafe/Cafe.app.main.
 *     Copyright (C) 2022  Yamin Siahmargooei
 *
 *     Cafe/Cafe.app.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cafe/Cafe.app.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cafe.  If not, see <https://www.gnu.org/licenses/>.
 */

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
import io.github.yamin8000.cafe.db.entities.order.Order
import io.github.yamin8000.cafe.db.entities.order.OrderDao
import io.github.yamin8000.cafe.db.entities.order.OrderDetail
import io.github.yamin8000.cafe.db.entities.order.OrderDetailDao
import io.github.yamin8000.cafe.db.entities.product.Product
import io.github.yamin8000.cafe.db.entities.product.ProductDao
import io.github.yamin8000.cafe.db.entities.relatives.RelativeDao
import io.github.yamin8000.cafe.db.entities.subscriber.Subscriber
import io.github.yamin8000.cafe.db.entities.subscriber.SubscriberDao
import io.github.yamin8000.cafe.db.entities.worker.Worker
import io.github.yamin8000.cafe.db.entities.worker.WorkerDao
import io.github.yamin8000.cafe.db.entities.worker.payment.Payment
import io.github.yamin8000.cafe.db.entities.worker.payment.PaymentDao
import io.github.yamin8000.cafe.db.entities.worker.schedule.Schedule
import io.github.yamin8000.cafe.db.entities.worker.schedule.ScheduleDao

@Database(
    entities = [
        Order::class,
        OrderDetail::class,
        Product::class,
        Day::class,
        Category::class,
        Subscriber::class,
        Account::class,
        Worker::class,
        Payment::class,
        Schedule::class
    ],
    version = 1
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun orderDetailDao(): OrderDetailDao
    abstract fun dayDao(): DayDao
    abstract fun productDao(): ProductDao
    abstract fun categoryDao(): CategoryDao
    abstract fun relativeDao(): RelativeDao
    abstract fun subscriberDao(): SubscriberDao
    abstract fun accountDao(): AccountDao
    abstract fun workerDao(): WorkerDao
    abstract fun paymentDao(): PaymentDao
    abstract fun scheduleDao(): ScheduleDao
}
