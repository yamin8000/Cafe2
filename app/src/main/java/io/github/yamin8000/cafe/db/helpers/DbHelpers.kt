package io.github.yamin8000.cafe.db.helpers

import io.github.yamin8000.cafe.db.entities.category.Category
import io.github.yamin8000.cafe.db.entities.product.Product
import io.github.yamin8000.cafe.util.Constants
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

object DbHelpers {

    suspend fun CoroutineContext.getProducts(): List<Product> {
        return withContext(this) {
            Constants.db?.productDao()?.getAll() ?: listOf()
        }
    }

    suspend fun CoroutineContext.getCategories(): List<Category> {
        return withContext(this) {
            Constants.db?.categoryDao()?.getAll() ?: listOf()
        }
    }
}