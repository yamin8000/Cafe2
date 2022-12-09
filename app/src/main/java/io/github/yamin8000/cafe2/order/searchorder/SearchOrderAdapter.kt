/*
 *     Cafe/Cafe.app.main
 *     SearchOrderAdapter.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     SearchOrderAdapter.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe2.order.searchorder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.yamin8000.cafe2.databinding.SearchOrderItemBinding
import io.github.yamin8000.cafe2.db.entities.relatives.OrderWithDetails
import io.github.yamin8000.cafe2.ui.recyclerview.AsyncDifferHelper.getAsyncDiffer

class SearchOrderAdapter(
    private val deliverListener: ((Long) -> Unit)? = null
) : RecyclerView.Adapter<SearchOrderHolder>() {

    val asyncList = this.getAsyncDiffer<OrderWithDetails, SearchOrderHolder>(
        { old, new -> old.orderAndSubscriber.order.id == new.orderAndSubscriber.order.id },
        { old, new -> old == new }
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchOrderHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = SearchOrderItemBinding.inflate(inflater, parent, false)
        return SearchOrderHolder(itemBinding, asyncList, deliverListener)
    }

    override fun onBindViewHolder(holder: SearchOrderHolder, position: Int) {
        val model = asyncList.currentList[position]

        holder.setOrderDate(model.orderAndSubscriber.order.date)
        holder.setOrderDayId(model.orderAndSubscriber.order.dayId)
        holder.setOrderStatus(model.orderAndSubscriber.order.status)
        holder.setOrderDetails(model.orderDetails)
        holder.setDescription(model.orderAndSubscriber.order.description)
        holder.setSubscriber(model.orderAndSubscriber.subscriber)
        holder.setTotalPrice(model.orderAndSubscriber.order.totalPrice)
    }

    override fun getItemCount() = asyncList.currentList.size

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position
}
