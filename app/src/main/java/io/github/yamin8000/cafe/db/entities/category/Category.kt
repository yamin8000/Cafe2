package io.github.yamin8000.cafe.db.entities.category

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    val name: String,
    val imageId: Int? = null,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
