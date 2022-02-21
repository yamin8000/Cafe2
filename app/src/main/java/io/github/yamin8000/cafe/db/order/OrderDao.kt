package io.github.yamin8000.cafe.db.order

import androidx.room.*

@Dao
interface OrderDao {

    @Query("select * from `order` order by id desc")
    suspend fun getAll(): List<Order>

    @Query("select * from `order` where id in (:ids)")
    suspend fun getAllByIds(vararg ids: Int): List<Order>

    @Query("select * from `order` where id = (:id)")
    suspend fun getById(id: Int): Order

    @Insert
    suspend fun insert(order: Order): Long

    @Insert
    suspend fun insertAll(vararg orders: Order): List<Long>

    @Delete
    suspend fun delete(order: Order)

    @Update
    suspend fun update(order: Order)
}