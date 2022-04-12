package io.github.yamin8000.cafe.report

import io.github.yamin8000.cafe.db.entities.relatives.OrderWithDetails
import io.github.yamin8000.cafe.db.entities.relatives.ProductAndCategory
import io.github.yamin8000.cafe.util.DateTimeUtils.toJalaliIso
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

    fun orderWithDetails(items: List<OrderWithDetails>): String {
        return csvOf(
            listOf(
                "Day Id",
                "DateTime",
                "Status",
                "Total Price",
                "Description",
                "Customer",
                "Order Details"
            ),
            items
        ) { item ->
            listOf(
                item.orderAndSubscriber.order.dayId.toString(),
                item.orderAndSubscriber.order.date.toJalaliIso(),
                item.orderAndSubscriber.order.status.name,
                item.orderAndSubscriber.order.totalPrice.toString(),
                item.orderAndSubscriber.order.description ?: "",
                item.orderAndSubscriber.subscriber.toString(),
                item.orderDetails.joinToString(" ") { it.summary }.trim()
            )
        }
    }
}