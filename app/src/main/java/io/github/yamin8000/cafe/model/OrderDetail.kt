package io.github.yamin8000.cafe.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OrderDetail(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    private val productId: Int,
    private val quantity: Int
)