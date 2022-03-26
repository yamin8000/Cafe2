package io.github.yamin8000.cafe.db.entities.order

import androidx.room.*

@Dao
interface OrderDao {

    @Query("select * from `order` order by id desc")
    suspend fun getAll(): List<Order>

    @Query("select * from `order` where id in (:ids)")
    suspend fun getAllByIds(vararg ids: Long): List<Order>

    @Query("select * from `order` where id = (:id)")
    suspend fun getById(id: Long): Order

    @Insert
    suspend fun insert(order: Order): Long

    @Insert
    suspend fun insertAll(vararg orders: Order): List<Long>

    @Delete
    suspend fun delete(order: Order)

    @Update
    suspend fun update(order: Order)
}