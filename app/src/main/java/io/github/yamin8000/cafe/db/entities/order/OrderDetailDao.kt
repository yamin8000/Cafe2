package io.github.yamin8000.cafe.db.entities.order

import androidx.room.Dao
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import io.github.yamin8000.cafe.db.entities.BaseDao

@Dao
abstract class OrderDetailDao : BaseDao<OrderDetail>("orderdetail") {

    @RawQuery
    protected abstract suspend fun getBestSelling(query: SupportSQLiteQuery): List<OrderDetail>

    suspend fun getBestSelling(): List<OrderDetail> {
        return getBestSelling(
            SimpleSQLiteQuery(
                """
            select productId , sum(quantity) as quantity, summary, orderId , description, id
            from orderdetail
            group by productId order by quantity desc
        """.trimIndent()
            )
        )
    }
}