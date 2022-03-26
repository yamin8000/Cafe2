package io.github.yamin8000.cafe.db.entities.order

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OrderDetail(
    val productId: Long,
    var quantity: Int = 0,
    val name: String,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)