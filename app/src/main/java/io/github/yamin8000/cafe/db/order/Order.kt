package io.github.yamin8000.cafe.db.order

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.yamin8000.cafe.model.OrderStatus
import java.time.LocalDateTime

@Entity
data class Order(
    val dayId: Int,
    val date: LocalDateTime,
    val detailIds: List<Int>,
    val status: OrderStatus = OrderStatus.Registered,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)