package io.github.yamin8000.cafe.db.entities.product

import androidx.room.*

@Dao
interface ProductDao {

    @Query("select * from `product`")
    suspend fun all(): List<Product>

    @Query("select * from `product` where id in (:ids)")
    suspend fun allByIds(vararg ids: Long): List<Product>

    @Query("select * from product where categoryId in (:categoryIds)")
    suspend fun allByCategoryIds(vararg categoryIds: Long): List<Product>

    @Query("select * from `product` where id = (:id)")
    suspend fun byId(id: Long): Product

    @Insert
    suspend fun insertAll(vararg products: Product)

    @Delete
    suspend fun delete(product: Product)

    @Query("delete from `product` where id = (:id)")
    suspend fun deleteById(id: Long)

    @Update
    suspend fun update(product: Product)
}