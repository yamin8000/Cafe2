package io.github.yamin8000.cafe.db.entities.relatives

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import io.github.yamin8000.cafe.db.entities.category.Category
import io.github.yamin8000.cafe.db.entities.product.Product
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductAndCategory(
    @Embedded
    val product: Product?,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: Category
) : Parcelable