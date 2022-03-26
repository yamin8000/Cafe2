package io.github.yamin8000.cafe.db.entities.product

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import io.github.yamin8000.cafe.db.entities.category.Category
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.RESTRICT
    )]
)
data class Product(
    var name: String,
    var price: Int,
    var categoryId: Int,
    var imageId: Int? = null,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
) : Parcelable
