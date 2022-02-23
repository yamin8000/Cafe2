package io.github.yamin8000.cafe.db.helpers

import io.github.yamin8000.cafe.db.order.Order
import io.github.yamin8000.cafe.db.order.OrderDetail
import io.github.yamin8000.cafe.db.product.Product
import io.github.yamin8000.cafe.util.Constants
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

object DbHelpers {

    suspend fun CoroutineContext.fetchOrderDetails(detailIds: List<Int>): List<OrderDetail> {
        return withContext(this) {
            val detailDao = Constants.db?.orderDetailDao()
            return@withContext detailDao?.getAllByIds(*detailIds.toIntArray())
        } ?: listOf()
    }

    suspend fun CoroutineContext.fetchOrders(): List<Order> {
        return withContext(this) {
            Constants.db?.orderDao()?.getAll()
        } ?: listOf()
    }

    suspend fun CoroutineContext.fetchProducts(): List<Product> {
        return withContext(this) {
            Constants.db?.productDao()?.getAll() ?: listOf()
        }
    }
}