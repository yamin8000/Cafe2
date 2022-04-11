package io.github.yamin8000.cafe.report

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import io.github.yamin8000.cafe.databinding.FragmentReportBinding
import io.github.yamin8000.cafe.db.entities.order.OrderStatus
import io.github.yamin8000.cafe.order.searchorder.SearchOrderAdapter
import io.github.yamin8000.cafe.ui.util.BaseFragment
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.handleCrash
import kotlinx.coroutines.launch

class ReportFragment : BaseFragment<FragmentReportBinding>({ FragmentReportBinding.inflate(it) }) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            fillReportsAutoComplete()
            lifecycleScope.launch { test() }
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    private fun fillReportsAutoComplete() {
        context?.let {

        }
    }

    private suspend fun test() {
        val orders = db?.relativeDao()?.getOrderWithDetails() ?: listOf()
        val reportOrders = orders.asSequence()
            .filter { it.orderAndSubscriber.order.status == OrderStatus.Registered }
            .filter { it.orderAndSubscriber.order.totalPrice < 5 }
            .toList()

        val adapter = SearchOrderAdapter()
        adapter.asyncList.submitList(reportOrders)
        binding.reportList.adapter = adapter
    }
}