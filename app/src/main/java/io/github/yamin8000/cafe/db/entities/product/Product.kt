package io.github.yamin8000.cafe.db.entities.product

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Product(
    var name: String,
    var price: Int,
    var categoryId: Int,
    var imageId: Int? = null,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
) : Parcelable
