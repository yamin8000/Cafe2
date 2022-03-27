package io.github.yamin8000.cafe.searchorder

import android.os.Bundle
import android.view.View
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentSearchOrdersBinding
import io.github.yamin8000.cafe.db.entities.relatives.OrderWithDetails
import io.github.yamin8000.cafe.model.OrderStatus
import io.github.yamin8000.cafe.ui.util.BaseFragment
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.Alerts.snack
import io.github.yamin8000.cafe.util.Utility.handleCrash
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
                val orders = db?.relativeDao()?.getOrderWithDetails() ?: emptyList()
                if (orders.isNotEmpty()) fillList(orders)
                else snack(getString(R.string.no_order_registered))
            }
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    private fun fillList(orders: List<OrderWithDetails>) {
        val adapter = SearchOrderAdapter(orders) { orderId ->
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
}