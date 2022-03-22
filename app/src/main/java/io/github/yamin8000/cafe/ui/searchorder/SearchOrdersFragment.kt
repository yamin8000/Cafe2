package io.github.yamin8000.cafe.ui.searchorder

import android.os.Bundle
import android.view.View
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentSearchOrdersBinding
import io.github.yamin8000.cafe.db.helpers.DbHelpers.fetchOrderDetails
import io.github.yamin8000.cafe.db.helpers.DbHelpers.fetchOrders
import io.github.yamin8000.cafe.db.entities.order.Order
import io.github.yamin8000.cafe.db.entities.order.OrderDetail
import io.github.yamin8000.cafe.model.OrderStatus
import io.github.yamin8000.cafe.ui.util.BaseFragment
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.handleCrash
import io.github.yamin8000.cafe.util.Utility.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchOrdersFragment :
    BaseFragment<FragmentSearchOrdersBinding>({ FragmentSearchOrdersBinding.inflate(it) }) {

    private val ioScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.IO) }
    private val mainScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.Main) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            mainScope.launch {
                val orders = ioScope.coroutineContext.fetchOrders()
                val detailIds = orders.flatMap { it.detailIds }
                val orderDetails = ioScope.coroutineContext.fetchOrderDetails(detailIds)
                if (orders.isNotEmpty()) fillList(orders, orderDetails)
                else handleEmptyOrders()
            }
        } catch (e: Exception) {
            handleCrash(e)
        }
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
}