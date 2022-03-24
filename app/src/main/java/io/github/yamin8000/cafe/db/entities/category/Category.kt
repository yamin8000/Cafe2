package io.github.yamin8000.cafe.db.entities.category

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    val name: String,
    val imageId: Int = 0,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
