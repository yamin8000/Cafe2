package io.github.yamin8000.cafe.report

import io.github.yamin8000.cafe.db.entities.relatives.ProductAndCategory
import io.github.yamin8000.cafe.util.Utility.csvOf

object ReportCSVs {

    fun productAndCategory(items: List<ProductAndCategory>): String {
        return csvOf(
            listOf(
                "Name",
                "Price",
                "Category"
            ),
            items
        ) { (product, category) ->
            listOf(
                product?.name ?: "",
                product?.price.toString(),
                category.name
            )
        }

    }
}