package io.github.yamin8000.cafe.searchorder

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentSearchOrdersBinding
import io.github.yamin8000.cafe.db.entities.relatives.OrderWithDetails
import io.github.yamin8000.cafe.model.OrderStatus
import io.github.yamin8000.cafe.ui.recyclerview.EmptyAdapter
import io.github.yamin8000.cafe.ui.util.BaseFragment
import io.github.yamin8000.cafe.util.Constants.db
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
            handleOrdersList()
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    private fun handleOrdersList() = mainScope.launch {
        val orders = db?.relativeDao()?.getOrderWithDetails() ?: emptyList()
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
        db?.orderDao()?.getById(orderId)?.let { order ->
            order.status = OrderStatus.Delivered
            db?.orderDao()?.update(order)
        }
    }
}