package io.github.yamin8000.cafe.db.order

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OrderDao {

    @Query("select * from `order`")
    suspend fun getAll(): List<Order>

    @Query("select * from `order` where id in (:ids)")
    suspend fun getAllByIds(vararg ids: Int): List<Order>

    @Query("select * from `order` where id = (:id)")
    suspend fun getById(id: Int): Order

    @Insert
    suspend fun insertAll(vararg orders: Order)

    @Delete
    suspend fun delete(order: Order)
}