package io.github.yamin8000.cafe.ui.searchorder

import android.os.Bundle
import android.view.View
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentSearchOrdersBinding
import io.github.yamin8000.cafe.db.order.Order
import io.github.yamin8000.cafe.db.order.OrderDetail
import io.github.yamin8000.cafe.model.OrderStatus
import io.github.yamin8000.cafe.ui.util.BaseFragment
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.handleCrash
import io.github.yamin8000.cafe.util.Utility.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchOrdersFragment :
    BaseFragment<FragmentSearchOrdersBinding>({ FragmentSearchOrdersBinding.inflate(it) }) {

    private val ioScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.IO) }
    private val mainScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.Main) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            mainScope.launch {
                val orders = fetchOrders()
                val detailIds = orders.flatMap { it.detailIds }
                val orderDetails = getOrderDetails(detailIds)
                if (orders.isNotEmpty()) fillList(orders, orderDetails)
                else handleEmptyOrders()
            }
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    private suspend fun getOrderDetails(detailIds: List<Int>): List<OrderDetail> {
        return withContext(ioScope.coroutineContext) {
            val details = listOf<OrderDetail>()
            val detailDao = db?.orderDetailDao()
            return@withContext detailDao?.getAllByIds(*detailIds.toIntArray())
        } ?: listOf()
    }

    private fun fillList(orders: List<Order>, orderDetails: List<OrderDetail>) {
        val adapter = SearchOrderAdapter(orders, orderDetails) { orderId ->
            ioScope.launch {
                val orderDao = db?.orderDao()
                val order = orderDao?.getById(orderId)
                order?.let {
                    it.status = OrderStatus.Delivered
                    orderDao.update(it)
                }
            }
        }
        binding.searchOrderList.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun handleEmptyOrders() {
        toast(getString(R.string.no_order_registered))
        //binding.textview = ???
    }

    private suspend fun fetchOrders(): List<Order> {
        return withContext(ioScope.coroutineContext) {
            db?.orderDao()?.getAll()
        } ?: listOf()
    }
}