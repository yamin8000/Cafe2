/*
 *     Cafe/Cafe.app.main
 *     BaseDao.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     BaseDao.kt Last modified at 2022/12/9
 *     This file is part of Cafe/Cafe.app.main.
 *     Copyright (C) 2022  Yamin Siahmargooei
 *
 *     Cafe/Cafe.app.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cafe/Cafe.app.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cafe.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.cafe2.db.entities

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

abstract class BaseDao<T>(tableName: String) {

    private val baseQuery = "select * from `$tableName`"

    private val baseWhereQuery = "$baseQuery where"

    /**
     * Inserts the given [entity] into the database and returns its id.
     */
    @Insert
    abstract suspend fun insert(entity: T): Long

    @Insert
    abstract suspend fun insertAll(entities: List<T>): List<Long>

    @Update
    abstract suspend fun update(entity: T): Int

    @Delete
    abstract suspend fun delete(entity: T): Int

    @Delete
    abstract suspend fun deleteAll(entities: List<T>): Int

    @RawQuery
    protected abstract suspend fun getById(query: SupportSQLiteQuery): T?

    suspend fun getById(id: Long): T? {
        return getById(SimpleSQLiteQuery("$baseQuery where id = $id"))
    }

    @RawQuery
    protected abstract suspend fun getAll(query: SupportSQLiteQuery): List<T>

    suspend fun getAll(): List<T> {
        return getAll(SimpleSQLiteQuery(baseQuery))
    }

    @RawQuery
    protected abstract suspend fun getAllByIds(query: SupportSQLiteQuery): List<T>

    suspend fun getAllByIds(ids: List<Long>): List<T> {
        val params = ids.joinToString("")
        return getAllByIds(SimpleSQLiteQuery("$baseWhereQuery id in ($params)"))
    }

    @RawQuery
    protected abstract suspend fun getByParam(query: SupportSQLiteQuery): List<T>

    suspend fun <P> getByParam(param: String, value: P): List<T> {
        return getByParam(SimpleSQLiteQuery("$baseWhereQuery $param = $value"))
    }

    suspend fun getByParams(vararg paramPairs: Pair<String, *>): List<T> {
        if (paramPairs.isNullOrEmpty()) return getAll()
        val condition = buildString {
            paramPairs.forEachIndexed { index, pair ->
                append("${pair.first} = '${pair.second}'")
                if (index == paramPairs.lastIndex && paramPairs.size != 1) append(" and ")
            }
        }
        return getByParam(SimpleSQLiteQuery("$baseWhereQuery $condition"))
    }
}