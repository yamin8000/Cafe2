/*
 *     Cafe/Cafe.app.main
 *     ReportCSVs.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     ReportCSVs.kt Last modified at 2022/12/9
 *     This file is part of Cafe/Cafe.app.main.
 *     Copyright (C) 2022  Yamin Siahmargooei
 *
 *     Cafe/Cafe.app.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cafe/Cafe.app.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cafe.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.cafe2.report

import io.github.yamin8000.cafe2.db.entities.relatives.OrderWithDetails
import io.github.yamin8000.cafe2.db.entities.relatives.ProductAndCategory
import io.github.yamin8000.cafe2.util.DateTimeUtils.toJalaliIso
import io.github.yamin8000.cafe2.util.Utility.csvOf

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