/*
 *     Cafe/Cafe.app.main
 *     FragmentOrderReport.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     FragmentOrderReport.kt Last modified at 2022/12/9
 *     This file is part of Cafe/Cafe.app.main.
 *     Copyright (C) 2022  Yamin Siahmargooei
 *
 *     Cafe/Cafe.app.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cafe/Cafe.app.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cafe.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.cafe2.report.reports

import android.os.Bundle
import android.view.View
import androidx.core.util.Pair
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.MaterialDatePicker
import io.github.yamin8000.cafe2.R
import io.github.yamin8000.cafe2.databinding.FragmentOrderReportBinding
import io.github.yamin8000.cafe2.db.entities.order.OrderStatus
import io.github.yamin8000.cafe2.db.entities.relatives.OrderWithDetails
import io.github.yamin8000.cafe2.db.entities.subscriber.Subscriber
import io.github.yamin8000.cafe2.order.searchorder.SearchOrderAdapter
import io.github.yamin8000.cafe2.report.ReportCSVs
import io.github.yamin8000.cafe2.ui.AutoArrayAdapter
import io.github.yamin8000.cafe2.util.Constants.db
import io.github.yamin8000.cafe2.util.DateTimeUtils.toJalaliIso
import io.github.yamin8000.cafe2.util.DateTimeUtils.toLocalDateTime
import io.github.yamin8000.cafe2.util.DateTimeUtils.toZonedDateTime
import io.github.yamin8000.cafe2.util.Utility.Views.handlePrice
import io.github.yamin8000.cafe2.util.Utility.Views.visible
import io.github.yamin8000.cafe2.util.Utility.handleCrash
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
            lifecycleScope.launch { handleArguments() }
            lifecycleScope.launch { fillSubscribersAutocomplete(db.subscriberDao().getAll()) }
            binding.orderReportCsv.setOnClickListener { shareCsv(ReportCSVs.orderWithDetails(items)) }
            datePickerListeners()
            menuHandler()
            subscriberClearHandler()
            binding.deliveredSwitch.setOnCheckedChangeListener { _, _ -> lifecycleScope.launch { handleArguments() } }
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    private fun subscriberClearHandler() {
        binding.orderReportSubscriberInput.setStartIconOnClickListener {
            subscriberId = null
            binding.orderReportSubscriberAuto.text?.clear()
            lifecycleScope.launch { handleArguments() }
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
        binding.orderDateRangePicker.setOnLongClickListener {
            clearDateRange()
            lifecycleScope.launch { handleArguments() }
            return@setOnLongClickListener true
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

    private fun clearDateRange() {
        startDateTime = null
        endDateTime = null
        binding.dateRangeText.text = ""
    }

    private fun handleDateTime(dateRange: Pair<Long, Long>) {
        startDateTime = handleStartDate(dateRange.first)
        endDateTime = handleEndDate(dateRange.second)
        binding.dateRangeText.visible()
        binding.dateRangeText.text = buildString {
            append(startDateTime?.toJalaliIso() ?: "")
            append("\n")
            append(endDateTime?.toJalaliIso() ?: "")
        }.trim()
        lifecycleScope.launch { handleArguments() }
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

    private fun fillSubscribersAutocomplete(subscribers: List<Subscriber>) {
        context?.let {
            AutoArrayAdapter(it, subscribers, binding.orderReportSubscriberAuto) { subscriber, _ ->
                subscriberId = subscriber.id
                lifecycleScope.launch { handleArguments() }
            }
        }
    }

    private fun Boolean.toOrderStatus(): OrderStatus {
        return if (this) OrderStatus.Delivered else OrderStatus.Registered
    }
}