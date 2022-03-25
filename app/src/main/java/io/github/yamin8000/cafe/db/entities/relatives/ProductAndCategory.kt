package io.github.yamin8000.cafe.db.entities.relatives

import androidx.room.Embedded
import androidx.room.Relation
import io.github.yamin8000.cafe.db.entities.category.Category
import io.github.yamin8000.cafe.db.entities.product.Product

data class ProductAndCategory(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val product: Product
)