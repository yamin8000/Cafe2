package io.github.yamin8000.cafe.db.entities.day

import androidx.room.Dao
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import io.github.yamin8000.cafe.db.entities.BaseDao
import java.time.LocalDate

@Dao
abstract class DayDao : BaseDao<Day>("Day") {

    @RawQuery
    protected abstract suspend fun getDay(query: SupportSQLiteQuery): Day

    suspend fun getByDate(date: LocalDate): Day {
        return getDay(SimpleSQLiteQuery("select * from day where date = $date"))
    }
}