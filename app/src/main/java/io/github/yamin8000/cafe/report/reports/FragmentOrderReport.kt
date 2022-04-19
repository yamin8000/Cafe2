package io.github.yamin8000.cafe.report.reports

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.util.Pair
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentOrderReportBinding
import io.github.yamin8000.cafe.db.entities.order.OrderStatus
import io.github.yamin8000.cafe.db.entities.relatives.OrderWithDetails
import io.github.yamin8000.cafe.db.entities.subscriber.Subscriber
import io.github.yamin8000.cafe.order.searchorder.SearchOrderAdapter
import io.github.yamin8000.cafe.report.ReportCSVs
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.DateTimeUtils.toJalaliIso
import io.github.yamin8000.cafe.util.DateTimeUtils.toLocalDateTime
import io.github.yamin8000.cafe.util.DateTimeUtils.toZonedDateTime
import io.github.yamin8000.cafe.util.Utility.Views.visible
import io.github.yamin8000.cafe.util.Utility.handleCrash
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

class FragmentOrderReport :
    BaseFragmentReport<OrderWithDetails, FragmentOrderReportBinding>({
        FragmentOrderReportBinding.inflate(it)
    }) {

    private var subscriberId: Long? = null
    private val orderAdapter by lazy(LazyThreadSafetyMode.NONE) { SearchOrderAdapter() }

    private var startDateTime: ZonedDateTime? = null
    private var endDateTime: ZonedDateTime? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            lifecycleScope.launch { getSubscribers() }
            binding.orderReportSearch.setOnClickListener {
                lifecycleScope.launch { handleArguments() }
            }
            binding.orderReportCsv.setOnClickListener {
                shareCsv(ReportCSVs.orderWithDetails(items))
            }
            datePickerListeners()
            menuHandler()
            subscriberClearHandler()
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    private fun subscriberClearHandler() {
        binding.orderReportSubscriberInput.setStartIconOnClickListener {
            subscriberId = null
            binding.orderReportSubscriberAuto.text?.clear()
        }
    }

    private fun menuHandler() {
        binding.reportOrderToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.newest_orders -> sortToNewOrders()
                R.id.oldest_orders -> sortToOldOrders()
                R.id.fattest_orders -> sortToFattestOrders()
                R.id.thinnest_orders -> sortToThinnestOrders()
            }
            true
        }
    }

    private fun sortToOldOrders() {
        items = items.sortedBy { it.orderAndSubscriber.order.date }
        createList(items)
    }

    private fun sortToFattestOrders() {
        items = items.sortedByDescending { it.orderAndSubscriber.order.totalPrice }
        createList(items)
    }

    private fun sortToThinnestOrders() {
        items = items.sortedBy { it.orderAndSubscriber.order.totalPrice }
        createList(items)
    }

    private fun sortToNewOrders() {
        items = items.sortedByDescending { it.orderAndSubscriber.order.date }
        createList(items)
    }

    private fun datePickerListeners() {
        binding.orderDateRangePicker.setOnLongClickListener { clearDateRange() }
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

    private fun clearDateRange(): Boolean {
        startDateTime = null
        endDateTime = null
        return false
    }

    private fun handleDateTime(dateRange: Pair<Long, Long>) {
        startDateTime = handleStartDate(dateRange.first)
        endDateTime = handleEndDate(dateRange.second)
        binding.dateRangeText.visible()
        binding.dateRangeText.text = buildString {
            append(startDateTime?.toJalaliIso() ?: "")
            append("\n")
            append(endDateTime?.toJalaliIso() ?: "")
        }
    }

    private fun handleStartDate(start: Long): ZonedDateTime {
        return start.toLocalDateTime()
            .withHour(0)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)
            .toZonedDateTime()
    }

    private fun handleEndDate(end: Long): ZonedDateTime {
        return end.toLocalDateTime()
            .withHour(23)
            .withMinute(59)
            .withSecond(59)
            .withNano(999999999)
            .toZonedDateTime()
    }

    override suspend fun getItems(): List<OrderWithDetails> {
        return db.relativeDao().getOrderWithDetails()
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
        if (subscriberId != null)
            orders = orders.filter { it.orderAndSubscriber.subscriber?.id == subscriberId }
        return orders
    }

    override fun createList(items: List<OrderWithDetails>) {
        if (items.isEmpty()) binding.orderReportList.adapter = emptyAdapter
        else binding.orderReportList.adapter = orderAdapter.apply { asyncList.submitList(items) }
    }

    private suspend fun getSubscribers() {
        val subscribers = db.subscriberDao().getAll()
        if (subscribers.isNotEmpty()) fillSubscribersAutocomplete(subscribers)
        else binding.orderReportSubscriberInput.isEnabled = false
    }

    private fun fillSubscribersAutocomplete(subscribers: List<Subscriber>) {
        context?.let {
            val adapter = ArrayAdapter(it, R.layout.dropdown_item, subscribers)
            binding.orderReportSubscriberAuto.apply {
                setAdapter(adapter)
                setOnItemClickListener { adapterView, _, position, _ ->
                    val subscriber = adapterView.adapter.getItem(position) as Subscriber
                    subscriberId = subscriber.id
                }
            }
        }
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