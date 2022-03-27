package io.github.yamin8000.cafe.db.entities.order

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import io.github.yamin8000.cafe.db.entities.subscriber.Subscriber
import io.github.yamin8000.cafe.model.OrderStatus
import java.time.ZonedDateTime

@Entity(
    foreignKeys = [ForeignKey(
        entity = Subscriber::class,
        parentColumns = ["id"],
        childColumns = ["subscriberId"],
        onDelete = ForeignKey.RESTRICT
    )]
)
data class Order(
    val dayId: Int,
    val date: ZonedDateTime,
    @ColumnInfo(name = "subscriberId", index = true)
    val subscriberId: Long? = null,
    val description: String? = null,
    var status: OrderStatus = OrderStatus.Registered,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)