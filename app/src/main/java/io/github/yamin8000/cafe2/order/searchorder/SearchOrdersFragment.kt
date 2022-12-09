/*
 *     Cafe/Cafe.app.main
 *     SearchOrdersFragment.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     SearchOrdersFragment.kt Last modified at 2022/12/9
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

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.yamin8000.cafe2.R
import io.github.yamin8000.cafe2.databinding.FragmentSearchOrdersBinding
import io.github.yamin8000.cafe2.db.entities.order.OrderStatus
import io.github.yamin8000.cafe2.db.entities.relatives.OrderWithDetails
import io.github.yamin8000.cafe2.ui.recyclerview.EmptyAdapter
import io.github.yamin8000.cafe2.ui.util.BaseFragment
import io.github.yamin8000.cafe2.util.Constants.db
import io.github.yamin8000.cafe2.util.Utility.handleCrash
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchOrdersFragment :
    BaseFragment<FragmentSearchOrdersBinding>({ FragmentSearchOrdersBinding.inflate(it) }) {

    private val ioScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.IO) }
    private val lifecycleScope by lazy(LazyThreadSafetyMode.NONE) { lifecycle.coroutineScope }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            handleOrdersList()
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    private fun handleOrdersList() = lifecycleScope.launch {
        val orders = db.relativeDao().getOrderWithDetails()
        if (orders.isNotEmpty()) showOrders(orders.asReversed())
        else showEmptyAdapter()
    }

    private fun showEmptyAdapter() {
        val adapter = EmptyAdapter(getString(R.string.no_order_registered))
        binding.searchOrderList.layoutManager = LinearLayoutManager(context)
        binding.searchOrderList.adapter = adapter
    }

    private fun showOrders(orders: List<OrderWithDetails>) {
        val adapter = SearchOrderAdapter(this::deliverOrder)
        binding.searchOrderList.adapter = adapter
        adapter.asyncList.submitList(orders)
    }

    private fun deliverOrder(orderId: Long) = ioScope.launch {
        db.orderDao().getById(orderId)?.let { order ->
            order.status = OrderStatus.Delivered
            db.orderDao().update(order)
            try {
                MediaPlayer.create(context, R.raw.bell).start()
            } catch (e: Exception) {
                //ignored
            }
        }
    }
}