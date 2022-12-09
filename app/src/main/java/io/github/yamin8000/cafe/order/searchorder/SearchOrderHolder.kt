/*
 *     Cafe/Cafe.app.main
 *     SearchOrderHolder.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     SearchOrderHolder.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe.order.searchorder

import android.text.TextUtils
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.SearchOrderItemBinding
import io.github.yamin8000.cafe.db.entities.order.OrderDetail
import io.github.yamin8000.cafe.db.entities.order.OrderStatus
import io.github.yamin8000.cafe.db.entities.relatives.OrderWithDetails
import io.github.yamin8000.cafe.db.entities.subscriber.Subscriber
import io.github.yamin8000.cafe.util.DateTimeUtils.toJalaliIso
import io.github.yamin8000.cafe.util.Utility.Views.gone
import io.github.yamin8000.cafe.util.Utility.Views.visible
import io.github.yamin8000.cafe.util.Utility.numFormat
import java.time.ZonedDateTime

class SearchOrderHolder(
    private val binding: SearchOrderItemBinding,
    private val asyncList: AsyncListDiffer<OrderWithDetails>,
    private val deliverListener: ((Long) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    private val textMaxLines = 3
    private val context = binding.root.context

    init {
        deliverClickEnablerHandler()
        deliverClickListenerHandler()
        detailExpandShrinkListener()
    }

    private fun detailExpandShrinkListener() {
        binding.searchOrderDetails.setOnClickListener {
            (it as TextView).let { text ->
                if (text.ellipsize == null) {
                    text.ellipsize = TextUtils.TruncateAt.END
                    text.maxLines = textMaxLines
                } else {
                    text.ellipsize = null
                    text.maxLines = Int.MAX_VALUE
                }
            }
        }
    }

    private fun deliverClickEnablerHandler() {
        if (adapterPosition != RecyclerView.NO_POSITION) {
            when (asyncList.currentList[adapterPosition].orderAndSubscriber.order.status) {
                OrderStatus.Registered -> binding.searchOrderDeliverButton.isEnabled = true
                OrderStatus.Delivered -> binding.searchOrderDeliverButton.isEnabled = false
            }
        }
    }

    private fun deliverClickListenerHandler() {
        if (deliverListener != null) binding.searchOrderDeliverButton.visible()
        else binding.searchOrderDeliverButton.gone()
        binding.searchOrderDeliverButton.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                binding.searchOrderDeliverButton.visible()
                deliverListener?.invoke(asyncList.currentList[adapterPosition].orderAndSubscriber.order.id)
                asyncList.currentList[adapterPosition].orderAndSubscriber.order.status =
                    OrderStatus.Delivered
                setOrderStatus(OrderStatus.Delivered)
            }
        }
    }

    fun setOrderDayId(dayId: Int) {
        binding.searchOrderDayId.text = dayId.toString()
    }

    fun setOrderDate(dateTime: ZonedDateTime) {
        binding.searchOrderDate.text = dateTime.toJalaliIso()
    }

    fun setOrderStatus(orderStatus: OrderStatus) {
        var statusText = ""
        when (orderStatus) {
            OrderStatus.Delivered -> {
                statusText = context.getString(R.string.delivered)
                binding.searchOrderDeliverButton.isEnabled = false
            }
            OrderStatus.Registered -> {
                statusText = context.getString(R.string.registered)
                binding.searchOrderDeliverButton.isEnabled = true
            }
        }
        binding.searchOrderStatus.text = statusText
    }

    fun setOrderDetails(orderDetailIds: List<OrderDetail>) {
        val detail = createSummaries(orderDetailIds)
        binding.searchOrderDetails.text = detail.trim()
    }

    private fun createSummaries(orderDetailIds: List<OrderDetail>): String {
        return buildString {
            orderDetailIds.forEach {
                this.append("${it.summary}\n")
            }
        }.trim()
    }

    fun setDescription(description: String?) {
        if (description == null) hideDescription()
        else showDescription(description)
    }

    private fun showDescription(description: String) {
        binding.searchOrderDescription.text = description
        binding.searchOrderDescriptionHolder.visible()
        binding.searchOrderDescription.visible()
    }

    private fun hideDescription() {
        binding.searchOrderDescription.gone()
        binding.searchOrderDescriptionHolder.gone()
    }

    fun setSubscriber(subscriber: Subscriber?) {
        if (subscriber == null) hideSubscriber()
        else showSubscriber(subscriber)
    }

    private fun showSubscriber(subscriber: Subscriber) {
        binding.searchOrderSubscriber.text = subscriber.toString()
        binding.searchOrderSubscriber.visible()
        binding.searchOrderSubscriberHolder.visible()
    }

    private fun hideSubscriber() {
        binding.searchOrderSubscriber.gone()
        binding.searchOrderSubscriberHolder.gone()
    }

    fun setTotalPrice(totalPrice: Long) {
        binding.searchOrderPrice.text = context.getString(
            R.string.rial_template,
            totalPrice.toString().numFormat()
        )
    }
}