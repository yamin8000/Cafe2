package io.github.yamin8000.cafe.neworder

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentNewOrderBinding
import io.github.yamin8000.cafe.db.entities.day.Day
import io.github.yamin8000.cafe.db.entities.order.Order
import io.github.yamin8000.cafe.db.entities.order.OrderDetail
import io.github.yamin8000.cafe.db.entities.product.Product
import io.github.yamin8000.cafe.db.helpers.DbHelpers.getProducts
import io.github.yamin8000.cafe.ui.util.BaseFragment
import io.github.yamin8000.cafe.util.Constants.PROMPT
import io.github.yamin8000.cafe.util.Constants.PROMPT_RESULT
import io.github.yamin8000.cafe.util.Constants.PROMPT_TEXT
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.Alerts.snack
import io.github.yamin8000.cafe.util.Utility.Alerts.toast
import io.github.yamin8000.cafe.util.Utility.handleCrash
import io.github.yamin8000.cafe.util.Utility.navigate
import ir.yamin.digits.Digits.Companion.spell
import kotlinx.coroutines.*
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

class NewOrderFragment :
    BaseFragment<FragmentNewOrderBinding>({ FragmentNewOrderBinding.inflate(it) }) {

    private val ioScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.IO) }
    private val mainScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.Main) }

    private var products = listOf<Product>()

    private var orderDetails = mutableListOf<OrderDetail>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            mainScope.launch {
                listHandler()
                saveOrderHandler()
            }
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    private fun saveOrderHandler() {
        binding.saveOrderButton.setOnClickListener {
            if (orderDetails.isNotEmpty()) {
                if (products.isNotEmpty()) {
                    val bundle = bundleOf(PROMPT_TEXT to getOrderDetails())
                    navigate(R.id.promptModal, bundle)
                    setFragmentResultListener(PROMPT) { _, args ->
                        if (args.getBoolean(PROMPT_RESULT)) mainScope.launch { handleOrderInsert() }
                        else snack(getString(R.string.order_saving_cancel))
                    }
                } else toast(getString(R.string.please_wait), Toast.LENGTH_LONG)
            } else snack(getString(R.string.order_is_empty), Toast.LENGTH_LONG)
        }
    }

    private suspend fun handleOrderInsert() {
        if (orderDetails.isNotEmpty()) {
            val orderId = createOrder()
            if (orderId != null) orderAddSuccess(orderId)
            else snack(getString(R.string.db_update_error))
        } else snack(getString(R.string.order_is_empty))
    }

    private suspend fun orderAddSuccess(orderId: Long) {
        insertOrderDetails(orderId)
        orderDetails.clear()
        snack(getString(R.string.item_add_success, getString(R.string.order)))
        findNavController().navigate(R.id.action_newOrderFragment_to_searchOrdersFragment)
    }

    private suspend fun insertOrderDetails(orderId: Long): List<Long> {
        val iterator = orderDetails.iterator()
        while (iterator.hasNext()) {
            val orderDetail = iterator.next()
            if (orderDetail.quantity == 0) iterator.remove()
            else orderDetail.orderId = orderId
        }
        return withContext(ioScope.coroutineContext) {
            db?.orderDetailDao()?.insertAll(orderDetails) ?: listOf()
        }
    }

    private suspend fun createOrder(): Long? {
        val today = LocalDate.now(ZoneId.systemDefault())

        val lastOrderId = ioScope.async {
            val dayDao = db?.dayDao()
            val day = dayDao?.getByParam("date", today.toEpochDay())?.firstOrNull()
            if (day != null) {
                day.lastOrderId++
                dayDao.update(day)
                return@async day.lastOrderId
            } else {
                dayDao?.insert(Day(today))
                return@async 1
            }
        }.await()

        val order = Order(lastOrderId, ZonedDateTime.now())

        val orderDao = db?.orderDao()
        return withContext(ioScope.coroutineContext) { orderDao?.insert(order) }
    }

    private suspend fun listHandler() {
        products = ioScope.coroutineContext.getProducts()
        val adapter = NewOrderDetailAdapter(products, this::itemChanged)
        binding.orderDetailList.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun itemChanged(pair: Pair<Long, Int>) {
        val (productId, quantity) = pair
        val oldItem = orderDetails.find { it.productId == productId }
        if (oldItem == null) orderDetails.add(createOrderDetail(productId, quantity))
        else updateOrderDetail(quantity, oldItem)
    }

    private fun updateOrderDetail(
        quantity: Int,
        oldItem: OrderDetail
    ) {
        if (quantity == 0) orderDetails.remove(oldItem)
        else oldItem.quantity = quantity
        oldItem.summary = createDetailSummary(oldItem.productId, quantity)
    }

    private fun createOrderDetail(
        productId: Long,
        quantity: Int
    ): OrderDetail {
        return OrderDetail(
            productId,
            quantity,
            createDetailSummary(productId, quantity)
        )
    }

    private fun getOrderDetails(): String {
        return buildString {
            orderDetails.forEach {
                append(createDetailSummary(it.productId, it.quantity))
                append("\n")
            }
        }
    }

    private fun createDetailSummary(
        productId: Long,
        quantity: Int
    ): String {
        val unit = getString(R.string.adad)
        return buildString {
            append(products.find { it.id == productId }?.name)
            append(" ")
            append("${quantity.spell()} $unit")
        }
    }
}