package io.github.yamin8000.cafe.db.entities.order

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.yamin8000.cafe.model.OrderStatus
import java.time.ZonedDateTime

@Entity
data class Order(
    val dayId: Int,
    val date: ZonedDateTime,
    val detailIds: List<Long>,
    var status: OrderStatus = OrderStatus.Registered,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)