package io.github.yamin8000.cafe.model

import io.github.yamin8000.cafe.db.product.Product

data class DetailJoinProduct(
    val product: Product,
    var quantity: Int,
    val orderId: Int,
    val id: Int
)