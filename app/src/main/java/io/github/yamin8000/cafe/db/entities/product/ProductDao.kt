package io.github.yamin8000.cafe.db.entities.product

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDao {

    @Query("select * from `product`")
    suspend fun all(): List<Product>

    @Query("select * from `product` where id in (:ids)")
    suspend fun allByIds(vararg ids: Int): List<Product>

    @Query("select * from product where categoryId in (:categoryIds)")
    suspend fun allByCategoryIds(vararg categoryIds: Int): List<Product>

    @Query("select * from `product` where id = (:id)")
    suspend fun byId(id: Int): Product

    @Insert
    suspend fun insertAll(vararg products: Product)

    @Delete
    suspend fun delete(product: Product)
}