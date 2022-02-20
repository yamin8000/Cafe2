package io.github.yamin8000.cafe.ui.neworder

import android.os.Bundle
import android.view.View
import android.widget.Toast
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentNewOrderBinding
import io.github.yamin8000.cafe.db.order.Day
import io.github.yamin8000.cafe.db.order.Order
import io.github.yamin8000.cafe.db.order.OrderDetail
import io.github.yamin8000.cafe.db.product.Product
import io.github.yamin8000.cafe.ui.util.BaseFragment
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.DateUtils
import io.github.yamin8000.cafe.util.Utility.handleCrash
import io.github.yamin8000.cafe.util.Utility.toast
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

    private suspend fun saveOrderHandler() {
        binding.saveOrderButton.setOnClickListener {
            if (products.isNotEmpty() && orderDetails.isNotEmpty() && db != null) {
                val orderDetailIds = mutableListOf<Int>()
                val orderDetailDao = db?.orderDetailDao()
                orderDetails.forEach {
                    ioScope.launch {
                        val detailId = withContext(ioScope.coroutineContext) {
                            orderDetailDao?.insert(it)
                        }
                        if (detailId != null) orderDetailIds.add(detailId.toInt())
                    }
                }

                mainScope.launch {
                    val (orderId, order) = createOrder(orderDetailIds)
                    if (orderId != null) {
                        toast(getString(R.string.order_created), Toast.LENGTH_LONG)
                        listHandler()
                        binding.lastOrderSummary.text = getString(
                            R.string.last_order_summary,
                            orderId.toString(),
                            DateUtils.isoOfDateTime(order.date),
                            order.status.toString()
                        )
                    } else toast(getString(R.string.error_creating_order))
                }

            } else toast(getString(R.string.db_null_error))
        }
    }

    private suspend fun createOrder(orderDetailIds: MutableList<Int>): Pair<Long?, Order> {
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
        val orderId = withContext(ioScope.coroutineContext) {
            orderDao?.insert(order)
        }
        return orderId to order
    }

    private suspend fun listHandler() {
        products = populateProducts()
        val adapter = NewOrderDetailAdapter(products, this::itemChanged)
        binding.orderDetailList.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun itemChanged(pair: Pair<Int, Int>) {
        //toast("${products.find { it.id == productId }?.name} == $quantity")
        val (productId, quantity) = pair
        val oldItem = orderDetails.find { it.productId == productId }
        if (oldItem == null) orderDetails.add(OrderDetail(productId, quantity))
        else oldItem.quantity = quantity
    }

    private suspend fun populateProducts(): List<Product> = withContext(ioScope.coroutineContext) {
        db?.productDao()?.getAll() ?: listOf()
    }
}