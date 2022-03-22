package io.github.yamin8000.cafe.db.entities.product

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    val name: String,
    val price: Int,
    val imageId: Int? = null,
    val categoryId: Int? = null,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)
