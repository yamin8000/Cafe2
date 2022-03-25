package io.github.yamin8000.cafe.db.entities.relatives

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface RelativeDao {

    @Transaction
    @Query("select * from product join category on product.categoryId == category.id")
    suspend fun allProductsAndCategories(): List<ProductAndCategory>

    @Transaction
    @Query("select * from product  join category on product.categoryId == category.id where product.id = :id")
    suspend fun allProductAndCategoryById(id: Int): ProductAndCategory

    @Transaction
    @Query("select * from product  join category on product.categoryId == category.id where product.id in (:ids)")
    suspend fun allProductsAndCategoriesByIds(vararg ids: Int): List<ProductAndCategory>
}