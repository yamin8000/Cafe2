/*
 *     Cafe/Cafe.app.main
 *     NewOrderFragment.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     NewOrderFragment.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe2.order.neworder

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.github.yamin8000.ppn.Digits.Companion.spell
import io.github.yamin8000.cafe2.R
import io.github.yamin8000.cafe2.databinding.FragmentNewOrderBinding
import io.github.yamin8000.cafe2.db.entities.day.Day
import io.github.yamin8000.cafe2.db.entities.order.Order
import io.github.yamin8000.cafe2.db.entities.order.OrderDetail
import io.github.yamin8000.cafe2.db.entities.product.Product
import io.github.yamin8000.cafe2.db.entities.relatives.ProductAndCategory
import io.github.yamin8000.cafe2.db.entities.subscriber.Subscriber
import io.github.yamin8000.cafe2.ui.util.BaseFragment
import io.github.yamin8000.cafe2.util.Constants.DATA
import io.github.yamin8000.cafe2.util.Constants.PROMPT
import io.github.yamin8000.cafe2.util.Constants.PROMPT_RESULT
import io.github.yamin8000.cafe2.util.Constants.PROMPT_TEXT
import io.github.yamin8000.cafe2.util.Constants.db
import io.github.yamin8000.cafe2.util.DateTimeUtils.zonedNow
import io.github.yamin8000.cafe2.util.Utility.Alerts.snack
import io.github.yamin8000.cafe2.util.Utility.handleCrash
import io.github.yamin8000.cafe2.util.Utility.navigate
import io.github.yamin8000.cafe2.util.Utility.numFormat
import kotlinx.coroutines.*
import java.time.LocalDate
import java.time.ZoneId

class NewOrderFragment :
    BaseFragment<FragmentNewOrderBinding>({ FragmentNewOrderBinding.inflate(it) }) {

    private val ioScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.IO) }
    private val lifecycleScope by lazy(LazyThreadSafetyMode.NONE) { lifecycle.coroutineScope }

    private var products = mutableSetOf<Product>()

    private var orderDetails = mutableListOf<OrderDetail>()

    private var subscriberId: Long? = null

    private var totalPrice = 0L

    private var detailSummaries = mutableMapOf<OrderDetail, String>()

    private val listAdapter = NewOrderDetailAdapter(this::itemChanged)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            initList()
            lifecycleScope.launch { loadSubscribers() }
            orderSavingHandler()
            binding.newOrderAddProduct.setOnClickListener { handleNewProducts() }
            fillList()
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    private fun initList() {
        binding.orderDetailList.adapter = listAdapter
        fillList()
    }

    private fun handleNewProducts() {
        navigate(R.id.action_newOrderFragment_to_newOrderNewProductModal)
        setFragmentResultListener(DATA) { _, bundle ->
            val newProducts = bundle.getParcelableArrayList<ProductAndCategory>(DATA)
            if (newProducts != null)
                products.addAll(newProducts.mapNotNull { it.product })
            fillList()
        }
    }

    private suspend fun loadSubscribers() {
        val subscribers = db.subscriberDao().getAll()
        if (subscribers.isNotEmpty()) fillAutoComplete(subscribers)
        else binding.subscriberInput.isEnabled = false
    }

    private fun fillAutoComplete(subscribers: List<Subscriber>) {
        context?.let {
            val adapter = ArrayAdapter(it, R.layout.dropdown_item, subscribers)
            binding.subscriberEdit.apply {
                setAdapter(adapter)
                setOnItemClickListener { adapterView, _, position, _ ->
                    val subscriber = adapterView.adapter.getItem(position) as Subscriber
                    subscriberId = subscriber.id
                }
            }
        }
    }

    private fun orderSavingHandler() {
        binding.saveOrderButton.setOnClickListener {
            if (orderDetails.isNotEmpty()) {
                if (products.isNotEmpty()) {
                    val summaries = createDetailSummaries()
                    detailSummaries = summaries.first
                    totalPrice = summaries.second
                    val bundle = bundleOf(PROMPT_TEXT to getOrderDetails())
                    navigate(R.id.promptModal, bundle)
                    setFragmentResultListener(PROMPT) { _, args ->
                        if (args.getBoolean(PROMPT_RESULT)) lifecycleScope.launch { beginInsertingOrder() }
                        else snack(getString(R.string.order_saving_cancel))
                    }
                } else snack(getString(R.string.please_wait))
            } else snack(getString(R.string.order_is_empty))
        }
    }

    private fun createDetailSummaries(): Pair<MutableMap<OrderDetail, String>, Long> {
        val summaries = mutableMapOf<OrderDetail, String>()
        var price = 0L
        orderDetails.forEach { detail ->
            products.find { it.id == detail.productId }?.let {
                val orderDetailPrice = detail.quantity * it.price
                val singleSummary = buildString {
                    append(it.name)
                    append("\n")
                    append(getString(R.string.adad_template, detail.quantity.spell()))
                    append("\n")
                    append(
                        getString(
                            R.string.rial_template,
                            orderDetailPrice.toString().numFormat()
                        )
                    )
                    append("\n\n")
                }
                summaries.putIfAbsent(detail, singleSummary)
                price += orderDetailPrice
                detail.summary = singleSummary
            }
        }
        return summaries to price
    }

    private suspend fun beginInsertingOrder() {
        if (orderDetails.isNotEmpty()) {
            val orderId = createOrder()
            orderAddSuccess(orderId)
        } else snack(getString(R.string.order_is_empty))
    }

    private suspend fun orderAddSuccess(orderId: Long) {
        insertOrderDetails(orderId)
        snack(getString(R.string.item_add_success, getString(R.string.order)))
        findNavController().navigate(R.id.action_newOrderFragment_to_searchOrdersFragment)
        try {
            MediaPlayer.create(context, R.raw.bell).start()
        } catch (e: Exception) {
            //ignored
        }
    }

    private suspend fun insertOrderDetails(orderId: Long) = withContext(ioScope.coroutineContext) {
        val iterator = orderDetails.iterator()
        while (iterator.hasNext()) {
            val orderDetail = iterator.next()
            if (orderDetail.quantity == 0) iterator.remove()
            else orderDetail.orderId = orderId
        }
        return@withContext db.orderDetailDao().insertAll(orderDetails)
    }

    private suspend fun createOrder(): Long {
        val today = LocalDate.now(ZoneId.systemDefault())

        val lastOrderId = ioScope.async {
            val dayDao = db.dayDao()
            val day = dayDao.getByParam("date", today.toEpochDay()).firstOrNull()
            if (day != null) {
                day.lastOrderId++
                dayDao.update(day)
                return@async day.lastOrderId
            } else {
                dayDao.insert(Day(today))
                return@async 1
            }
        }.await()

        val order = Order(lastOrderId, zonedNow(), totalPrice, subscriberId, getDescription())

        val orderDao = db.orderDao()
        return withContext(ioScope.coroutineContext) { orderDao.insert(order) }
    }

    private fun getDescription(): String? {
        val text = binding.descriptionEdit.text?.toString()
        return if (text.isNullOrEmpty()) null else text
    }

    private fun fillList() {
        listAdapter.asyncList.submitList(products.toList())
    }

    private fun itemChanged(pair: Pair<Product, Int>) {
        val (product, quantity) = pair
        val oldItem = orderDetails.find { it.productId == product.id }
        if (oldItem == null) orderDetails.add(OrderDetail(product.id, quantity))
        else updateOrderDetail(quantity, oldItem)
    }

    private fun updateOrderDetail(
        quantity: Int,
        oldItem: OrderDetail
    ) {
        if (quantity == 0) orderDetails.remove(oldItem)
        else oldItem.quantity = quantity
    }

    private fun getOrderDetails(): String {
        return buildString {
            detailSummaries.forEach { (_, summary) ->
                append(summary)
                append("\n")
            }
            append(
                getString(
                    R.string.rial_template,
                    getString(R.string.total_price_template, totalPrice.toString())
                )
            )
        }.trim()
    }
}