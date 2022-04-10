package io.github.yamin8000.cafe.db.entities.relatives

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface RelativeDao {

    @Transaction
    @Query("select * from product")
    suspend fun getProductsAndCategories(): List<ProductAndCategory>

    @Transaction
    @Query("select * from `order`")
    suspend fun getOrderWithDetails(): List<OrderWithDetails>

    @Transaction
    @Query("select * from `payment`")
    suspend fun getPaymentsAndWorkers(): List<PaymentAndWorker>

    @Transaction
    @Query("select * from `schedule`")
    suspend fun getSchedulesAndWorkers(): List<ScheduleAndWorker>
}