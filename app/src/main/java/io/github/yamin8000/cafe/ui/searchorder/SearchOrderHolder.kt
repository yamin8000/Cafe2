package io.github.yamin8000.cafe.ui.searchorder

import androidx.recyclerview.widget.RecyclerView
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.SearchOrderItemBinding
import io.github.yamin8000.cafe.db.entities.order.Order
import io.github.yamin8000.cafe.db.entities.order.OrderDetail
import io.github.yamin8000.cafe.model.OrderStatus
import io.github.yamin8000.cafe.util.DateTimeUtils.toIso
import ir.yamin.digits.Digits.Companion.spell
import java.time.LocalDateTime

class SearchOrderHolder(
    private val binding: SearchOrderItemBinding,
    private val orders: List<Order>,
    private val orderDetails: List<OrderDetail>,
    private val deliverListener: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val context = binding.root.context

    init {
        if (adapterPosition != RecyclerView.NO_POSITION) {
            when (orders[adapterPosition].status) {
                OrderStatus.Registered -> binding.searchOrderDeliverButton.isEnabled = true
                OrderStatus.Delivered -> binding.searchOrderDeliverButton.isEnabled = false
            }
        }
        deliverClickListener()
    }

    private fun deliverClickListener() {
        binding.searchOrderDeliverButton.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                deliverListener(orders[adapterPosition].id)
                orders[adapterPosition].status = OrderStatus.Delivered
                setOrderStatus(OrderStatus.Delivered)
            }
        }
    }

    fun setOrderDayId(dayId: Int) {
        binding.searchOrderDayId.text = context.getString(R.string.order_day_id, dayId.toString())
    }

    fun setOrderDate(dateTime: LocalDateTime) {
        binding.searchOrderDate.text = context.getString(
            R.string.order_date,
            dateTime.toIso()
        )
    }

    fun setOrderStatus(orderStatus: OrderStatus) {
        var statusText = ""
        when (orderStatus) {
            OrderStatus.Delivered -> {
                statusText = context.getString(R.string.delivered)
                binding.searchOrderDeliverButton.isEnabled = false
            }
            OrderStatus.Registered -> {
                statusText = context.getString(R.string.registered)
                binding.searchOrderDeliverButton.isEnabled = true
            }
        }
        binding.searchOrderStatus.text = context.getString(R.string.order_status, statusText)
    }

    fun setOrderDetails(orderDetailIds: List<Int>) {
        val unit = context.getString(R.string.adad)
        val candidDetails = orderDetails.filter { it.id in orderDetailIds }
        val detail = buildString {
            candidDetails.forEach { this.append("${it.name} ==> ${it.quantity.spell()} $unit\n") }
        }
        binding.searchOrderDetails.text = context.getString(R.string.order_details, detail)
    }
}