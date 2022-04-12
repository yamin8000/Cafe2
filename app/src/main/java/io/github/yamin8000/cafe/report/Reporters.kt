package io.github.yamin8000.cafe.report

import io.github.yamin8000.cafe.db.entities.relatives.ProductAndCategory
import io.github.yamin8000.cafe.util.Constants.db

object Reporters {

    suspend fun getProductReports(
        name: String?,
        category: String?,
        price: String?
    ): List<ProductAndCategory> {
        var products = getProducts()

        if (name != null) products = products.filter { it.product?.name?.contains(name) ?: false }
        if (category != null) products = products.filter { it.category.name.contains(category) }
        if (price != null) products = products.filter { it.product?.price ?: 0 <= price.toLong() }

        return products
    }

    private suspend fun getProducts(): List<ProductAndCategory> {
        return db?.relativeDao()
            ?.getProductsAndCategories()
            ?: listOf()
    }
}