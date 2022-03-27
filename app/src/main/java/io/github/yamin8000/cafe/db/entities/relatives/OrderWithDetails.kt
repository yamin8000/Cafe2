package io.github.yamin8000.cafe.db.entities.relatives

import androidx.room.Embedded
import androidx.room.Relation
import io.github.yamin8000.cafe.db.entities.order.Order
import io.github.yamin8000.cafe.db.entities.order.OrderDetail

data class OrderWithDetails(
    @Embedded
    val order: Order,
    @Relation(
        parentColumn = "id",
        entityColumn = "orderId"
    )
    val orderDetails: List<OrderDetail>
)