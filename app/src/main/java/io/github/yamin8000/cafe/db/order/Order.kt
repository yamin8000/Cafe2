package io.github.yamin8000.cafe.db.order

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import io.github.yamin8000.cafe.model.OrderStatus
import java.time.LocalDateTime

@Entity
data class Order(
    val dayId: Int,
    val date: LocalDateTime,
    val status: OrderStatus,
    val detailIds: List<Int>,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)