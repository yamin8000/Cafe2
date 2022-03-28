package io.github.yamin8000.cafe.db.entities.relatives

import androidx.room.Embedded
import androidx.room.Relation
import io.github.yamin8000.cafe.db.entities.order.Order
import io.github.yamin8000.cafe.db.entities.subscriber.Subscriber

data class OrderAndSubscriber(
    @Embedded
    val order: Order,
    @Relation(
        parentColumn = "subscriberId",
        entityColumn = "id"
    )
    val subscriber: Subscriber?
)