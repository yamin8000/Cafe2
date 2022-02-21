package io.github.yamin8000.cafe.model

import io.github.yamin8000.cafe.db.order.OrderDetail
import java.time.LocalDateTime

data class OrderJoinDetail(
    val dayId: Int,
    val date: LocalDateTime,
    val status: OrderStatus = OrderStatus.Registered,
    val id: Int,
    val orderDetails: List<OrderDetail>
)
