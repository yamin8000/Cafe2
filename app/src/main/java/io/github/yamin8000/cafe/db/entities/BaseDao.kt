package io.github.yamin8000.cafe.db.entities

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

abstract class BaseDao<T>(private val tableName: String) {

    val baseQuery = "select * from $tableName"

    @Insert
    abstract suspend fun insert(entity: T): Long

    @Insert
    abstract suspend fun insertAll(entities: List<T>): List<Long>

    @Update
    abstract suspend fun update(entity: T): Int

    @Delete
    abstract suspend fun delete(entity: T): Int

    @RawQuery
    protected abstract suspend fun getById(query: SupportSQLiteQuery): T?

    suspend fun getById(id: Long): T? {
        return getById(SimpleSQLiteQuery("$baseQuery where id = $id"))
    }

    @RawQuery
    protected abstract suspend fun getAll(query: SupportSQLiteQuery): List<T>

    suspend fun getAll(): List<T> {
        return getAll(SimpleSQLiteQuery("$baseQuery $tableName"))
    }

    @RawQuery
    protected abstract suspend fun getAllByIds(query: SupportSQLiteQuery): List<T>

    suspend fun getAllByIds(ids: List<Long>): List<T> {
        val params = "(${ids.joinToString("")})"
        return getAllByIds(SimpleSQLiteQuery("$baseQuery where id in $params"))
    }

    @RawQuery
    protected abstract suspend fun getByParam(query: SupportSQLiteQuery): List<T>

    suspend fun <P> getByParam(param: String, value: P): List<T> {
        return getByParam(SimpleSQLiteQuery("select * from $tableName where $param == $value"))
    }
}