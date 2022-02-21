package io.github.yamin8000.cafe.db.order

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OrderDetail(
    val productId: Int,
    var quantity: Int = 0,
    val name: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)