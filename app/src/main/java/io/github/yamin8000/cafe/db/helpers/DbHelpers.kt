package io.github.yamin8000.cafe.db.helpers

import io.github.yamin8000.cafe.db.entities.category.Category
import io.github.yamin8000.cafe.db.entities.order.Order
import io.github.yamin8000.cafe.db.entities.order.OrderDetail
import io.github.yamin8000.cafe.db.entities.product.Product
import io.github.yamin8000.cafe.util.Constants
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

object DbHelpers {

    suspend fun CoroutineContext.getOrderDetails(detailIds: List<Long>): List<OrderDetail> {
        return withContext(this) {
            val detailDao = Constants.db?.orderDetailDao()
            return@withContext detailDao?.getAllByIds(*detailIds.toLongArray())
        } ?: listOf()
    }

    suspend fun CoroutineContext.getOrders(): List<Order> {
        return withContext(this) {
            Constants.db?.orderDao()?.getAll()
        } ?: listOf()
    }

    suspend fun CoroutineContext.getProducts(): List<Product> {
        return withContext(this) {
            Constants.db?.productDao()?.all() ?: listOf()
        }
    }

    suspend fun CoroutineContext.getCategories(): List<Category> {
        return withContext(this) {
            Constants.db?.categoryDao()?.getAll() ?: listOf()
        }
    }

    suspend fun CoroutineContext.newCategory(category: Category): Long {
        return withContext(this) {
            Constants.db?.categoryDao()?.insert(category) ?: -1
        }
    }

    suspend fun CoroutineContext.deleteCategories(vararg category: Category) {
        withContext(this) {
            category.forEach { Constants.db?.categoryDao()?.delete(it) }
        }
    }
}