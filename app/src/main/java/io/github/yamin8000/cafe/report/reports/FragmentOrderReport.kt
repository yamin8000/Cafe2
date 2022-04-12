package io.github.yamin8000.cafe.report.reports

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import io.github.yamin8000.cafe.databinding.FragmentOrderReportBinding
import io.github.yamin8000.cafe.db.entities.order.OrderStatus
import io.github.yamin8000.cafe.db.entities.relatives.OrderWithDetails
import io.github.yamin8000.cafe.order.searchorder.SearchOrderAdapter
import io.github.yamin8000.cafe.report.ReportCSVs
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.handleCrash
import kotlinx.coroutines.launch

class FragmentOrderReport :
    BaseFragmentReport<OrderWithDetails, FragmentOrderReportBinding>({
        FragmentOrderReportBinding.inflate(it)
    }) {

    private val orderAdapter by lazy(LazyThreadSafetyMode.NONE) { SearchOrderAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            binding.orderFilterConfirm.setOnClickListener {
                lifecycleScope.launch { handleArguments() }
            }
            binding.orderReportCsv.setOnClickListener {
                shareCsv(ReportCSVs.orderWithDetails(items))
            }
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    override suspend fun getItems(): List<OrderWithDetails> {
        return db?.relativeDao()?.getOrderWithDetails() ?: listOf()
    }

    override suspend fun filterItems(
        items: List<OrderWithDetails>,
        arguments: Bundle?
    ): List<OrderWithDetails> {
        val isDelivered = binding.deliveredSwitch.isChecked
        val minPrice = binding.orderMinPriceEdit.handlePrice()
        val maxPrice = binding.orderMaxPriceEdit.handlePrice()
        var orders = items
            .filter { it.orderAndSubscriber.order.status == isDelivered.toOrderStatus() }
        if (minPrice != null)
            orders = orders.filter { it.orderAndSubscriber.order.totalPrice >= minPrice }
        if (maxPrice != null)
            orders = orders.filter { it.orderAndSubscriber.order.totalPrice <= maxPrice }
        return orders
    }

    override fun createList(items: List<OrderWithDetails>) {
        if (items.isEmpty()) binding.orderReportList.adapter = emptyAdapter
        else binding.orderReportList.adapter = orderAdapter.apply { asyncList.submitList(items) }
    }

    private fun TextInputEditText.handlePrice(): Long? {
        return try {
            this.text.toString().let { if (it.isNotBlank()) it.toLong() else null }
        } catch (e: NumberFormatException) {
            return 0
        }
    }

    private fun Boolean.toOrderStatus(): OrderStatus {
        return if (this) OrderStatus.Delivered else OrderStatus.Registered
    }
}