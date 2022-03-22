package io.github.yamin8000.cafe.db.entities.day

import androidx.room.*
import java.time.LocalDate

@Dao
interface DayDao {

    @Query("select * from `day`")
    suspend fun getAll(): List<Day>

    @Query("select * from `day` where id in (:ids)")
    suspend fun getAllByIds(vararg ids: Int): List<Day>

    @Query("select * from `day` where id = (:id)")
    suspend fun getById(id: Int): Day

    @Query("select * from 'day' where date= (:date)")
    suspend fun getByDate(date: LocalDate): Day

    @Insert
    suspend fun insert(day: Day): Long

    @Insert
    suspend fun insertAll(vararg days: Day): List<Long>

    @Delete
    suspend fun delete(day: Day)

    @Update
    suspend fun update(day: Day)
}