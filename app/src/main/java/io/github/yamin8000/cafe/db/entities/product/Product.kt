package io.github.yamin8000.cafe.db.entities.product

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    var name: String,
    var price: Int,
    var categoryId: Int,
    var imageId: Int? = null,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)
