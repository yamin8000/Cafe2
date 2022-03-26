package io.github.yamin8000.cafe.neworder

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentNewOrderBinding
import io.github.yamin8000.cafe.db.entities.day.Day
import io.github.yamin8000.cafe.db.entities.order.Order
import io.github.yamin8000.cafe.db.entities.order.OrderDetail
import io.github.yamin8000.cafe.db.entities.product.Product
import io.github.yamin8000.cafe.db.helpers.DbHelpers.getProducts
import io.github.yamin8000.cafe.ui.util.BaseFragment
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.Alerts.toast
import io.github.yamin8000.cafe.util.Utility.handleCrash
import ir.yamin.digits.Digits.Companion.spell
import kotlinx.coroutines.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

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
                val bundle = bundleOf("details" to getOrderDetails())
                findNavController().navigate(
                    R.id.action_newOrderFragment_to_singleNewOrderModal,
                    bundle
                )
                when {
                    products.isEmpty() -> toast(getString(R.string.please_wait), Toast.LENGTH_LONG)
                    db == null -> toast(getString(R.string.db_null_error), Toast.LENGTH_LONG)
                    else -> {
                        modalObserver(
                            { saveOrder() },
                            { toast(getString(R.string.order_saving_cancel)) })
                    }
                }
            } else toast(getString(R.string.order_is_empty), Toast.LENGTH_LONG)
        }
    }

    private fun saveOrder() {
        val orderDetailIds = mutableListOf<Long>()
        val orderDetailDao = db?.orderDetailDao()
        if (orderDetails.isNotEmpty()) {
            orderDetails.forEach {
                if (it.quantity > 0) {
                    ioScope.launch {
                        val detailId = withContext(ioScope.coroutineContext) {
                            orderDetailDao?.insert(it)
                        }
                        if (detailId != null) orderDetailIds.add(detailId.toLong())
                    }
                }
            }

            mainScope.launch {
                createOrder(orderDetailIds)
                toast(getString(R.string.order_created), Toast.LENGTH_LONG)
                findNavController().navigate(R.id.action_newOrderFragment_to_searchOrdersFragment)
            }
        } else toast(getString(R.string.order_is_empty), Toast.LENGTH_LONG)
    }

    private fun modalObserver(success: () -> Unit, fail: () -> Unit) {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.singleNewOrderModal)
        val observer = LifecycleEventObserver { _, event ->
            try {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    if (navBackStackEntry.savedStateHandle.contains("prompt")) {
                        val isSaving =
                            navBackStackEntry.savedStateHandle.get<Boolean>("prompt") ?: false
                        if (isSaving)
                            success()
                        else fail()
                    }
                }
            } catch (e: Exception) {
                fail()
            }
        }
        navBackStackEntry.lifecycle.addObserver(observer)

        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })
    }

    private suspend fun createOrder(orderDetailIds: MutableList<Long>): Pair<Int, Order> {
        val today = LocalDate.now(ZoneOffset.UTC)

        val lastOrderId = ioScope.async {
            val dayDao = db?.dayDao()
            val day = dayDao?.getByDate(today)
            if (day != null) {
                day.lastOrderId++
                dayDao.update(day)
                return@async day.lastOrderId
            } else {
                dayDao?.insert(Day(today))
                return@async 1
            }
        }.await()

        val order = Order(lastOrderId, LocalDateTime.now(ZoneOffset.UTC), orderDetailIds)

        val orderDao = db?.orderDao()
        withContext(ioScope.coroutineContext) { orderDao?.insert(order) }
        orderDetailIds.clear()
        orderDetails.clear()
        return lastOrderId to order
    }

    private suspend fun listHandler() {
        products = ioScope.coroutineContext.getProducts()
        val adapter = NewOrderDetailAdapter(products, this::itemChanged)
        binding.orderDetailList.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun itemChanged(pair: Pair<Long, Int>) {
        val (productId, quantity) = pair
        val productName = products.find { it.id == productId }?.name ?: ""
        val oldItem = orderDetails.find { it.productId == productId }
        if (oldItem == null) orderDetails.add(OrderDetail(productId, quantity, productName))
        else {
            if (quantity == 0) orderDetails.remove(oldItem)
            else oldItem.quantity = quantity
        }
    }

    private fun getOrderDetails(): String {
        val unit = getString(R.string.adad)
        val detail = buildString {
            orderDetails.forEach { this.append("${it.name} ==> ${it.quantity.spell()} $unit\n") }
        }
        return getString(R.string.order_details, detail)
    }
}