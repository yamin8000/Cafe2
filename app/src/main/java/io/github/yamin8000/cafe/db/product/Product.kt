package io.github.yamin8000.cafe.db.product

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    val name: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)
