package io.github.yamin8000.cafe.report.reports

import android.os.Bundle
import android.view.View
import androidx.core.util.Pair
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentOrderReportBinding
import io.github.yamin8000.cafe.db.entities.order.OrderStatus
import io.github.yamin8000.cafe.db.entities.relatives.OrderWithDetails
import io.github.yamin8000.cafe.order.searchorder.SearchOrderAdapter
import io.github.yamin8000.cafe.report.ReportCSVs
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.DateTimeUtils.toJalaliIso
import io.github.yamin8000.cafe.util.DateTimeUtils.zonedDateTimeOfMillis
import io.github.yamin8000.cafe.util.Utility.handleCrash
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

class FragmentOrderReport :
    BaseFragmentReport<OrderWithDetails, FragmentOrderReportBinding>({
        FragmentOrderReportBinding.inflate(it)
    }) {

    private val orderAdapter by lazy(LazyThreadSafetyMode.NONE) { SearchOrderAdapter() }

    private var startDateTime: ZonedDateTime? = null
    private var endDateTime: ZonedDateTime? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            binding.orderReportSearch.setOnClickListener {
                lifecycleScope.launch { handleArguments() }
            }
            binding.orderReportCsv.setOnClickListener {
                shareCsv(ReportCSVs.orderWithDetails(items))
            }
            datePickerListeners()
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    private fun datePickerListeners() {
        binding.orderDateRangePicker.setOnLongClickListener {
            startDateTime = null
            endDateTime = null
            binding.orderDateRangePicker.text = getString(R.string.select_date)
            false
        }
        binding.orderDateRangePicker.setOnClickListener {
            val picker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText(getString(R.string.select_date))
                .setSelection(
                    Pair(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                    )
                ).build()
            picker.show(childFragmentManager, null)
            picker.addOnPositiveButtonClickListener { handleDateTime(it) }
        }
    }

    private fun handleDateTime(dateRange: Pair<Long, Long>) {
        startDateTime = dateRange.first.zonedDateTimeOfMillis()
        endDateTime = dateRange.second.zonedDateTimeOfMillis()
        binding.orderDateRangePicker.text = buildString {
            append(startDateTime?.toJalaliIso() ?: "")
            append("\n")
            append(endDateTime?.toJalaliIso() ?: "")
        }
    }

    override suspend fun getItems(): List<OrderWithDetails> {
        return db?.relativeDao()?.getOrderWithDetails() ?: listOf()
    }

    override suspend fun filterItems(items: List<OrderWithDetails>): List<OrderWithDetails> {
        val isDelivered = binding.deliveredSwitch.isChecked
        val minPrice = binding.orderMinPriceEdit.handlePrice()
        val maxPrice = binding.orderMaxPriceEdit.handlePrice()
        var orders = items
            .filter { it.orderAndSubscriber.order.status == isDelivered.toOrderStatus() }
        if (minPrice != null)
            orders = orders.filter { it.orderAndSubscriber.order.totalPrice >= minPrice }
        if (maxPrice != null)
            orders = orders.filter { it.orderAndSubscriber.order.totalPrice <= maxPrice }
        if (startDateTime != null)
            orders = orders.filter { it.orderAndSubscriber.order.date.isAfter(startDateTime) }
        if (endDateTime != null)
            orders = orders.filter { it.orderAndSubscriber.order.date.isBefore(endDateTime) }
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