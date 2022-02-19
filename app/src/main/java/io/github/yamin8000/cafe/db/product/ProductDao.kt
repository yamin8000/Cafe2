package io.github.yamin8000.cafe.db.product

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDao {

    @Query("select * from `product`")
    suspend fun getAll(): List<Product>

    @Query("select * from `product` where id in (:ids)")
    suspend fun getAllByIds(vararg ids: Int): List<Product>

    @Query("select * from `product` where id = (:id)")
    suspend fun getById(id: Int): Product

    @Insert
    suspend fun insertAll(vararg products: Product)

    @Delete
    suspend fun delete(product: Product)
}