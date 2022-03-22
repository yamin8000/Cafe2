package io.github.yamin8000.cafe.db.entities.category

import androidx.room.*

@Dao
interface CategoryDao {

    @Query("select * from category")
    suspend fun getAll(): List<Category>

    @Query("select * from category where id in (:ids)")
    suspend fun getAllByIds(vararg ids: Int): List<Category>

    @Query("select * from category where id = (:id)")
    suspend fun getById(id: Int): Category

    @Query("select * from category where name = (:name)")
    suspend fun getByName(name: String): Category

    @Insert
    suspend fun insert(category: Category): Long

    @Insert
    suspend fun insertAll(vararg categories: Category): List<Long>

    @Delete
    suspend fun delete(category: Category)

    @Update
    suspend fun update(category: Category)
}