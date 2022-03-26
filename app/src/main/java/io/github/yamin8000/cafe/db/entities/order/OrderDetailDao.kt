package io.github.yamin8000.cafe.db.entities.order

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OrderDetailDao {

    @Query("select * from `orderdetail`")
    suspend fun getAll(): List<OrderDetail>

    @Query("select * from `orderdetail` where id in (:ids)")
    suspend fun getAllByIds(vararg ids: Long): List<OrderDetail>

    @Query("select * from `orderdetail` where id = (:id)")
    suspend fun getById(id: Long): OrderDetail

    @Insert
    suspend fun insert(orderDetail: OrderDetail): Long

    @Insert
    suspend fun insertAll(vararg orderDetails: OrderDetail): List<Long>

    @Delete
    suspend fun delete(orderDetail: OrderDetail)
}