package io.github.yamin8000.cafe.searchorder

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.SearchOrderItemBinding
import io.github.yamin8000.cafe.db.entities.order.OrderDetail
import io.github.yamin8000.cafe.db.entities.relatives.OrderWithDetails
import io.github.yamin8000.cafe.db.entities.subscriber.Subscriber
import io.github.yamin8000.cafe.model.OrderStatus
import io.github.yamin8000.cafe.util.DateTimeUtils.toJalaliIso
import io.github.yamin8000.cafe.util.Utility.Views.gone
import io.github.yamin8000.cafe.util.Utility.Views.visible
import java.time.ZonedDateTime

class SearchOrderHolder(
    private val binding: SearchOrderItemBinding,
    private val asyncList: AsyncListDiffer<OrderWithDetails>,
    private val deliverListener: (Long) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val context = binding.root.context

    init {
        deliverClickEnablerHandler()
        deliverClickListener()
    }

    private fun deliverClickEnablerHandler() {
        if (adapterPosition != RecyclerView.NO_POSITION) {
            when (asyncList.currentList[adapterPosition].orderAndSubscriber.order.status) {
                OrderStatus.Registered -> binding.searchOrderDeliverButton.isEnabled = true
                OrderStatus.Delivered -> binding.searchOrderDeliverButton.isEnabled = false
            }
        }
    }

    private fun deliverClickListener() {
        binding.searchOrderDeliverButton.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                deliverListener(asyncList.currentList[adapterPosition].orderAndSubscriber.order.id)
                asyncList.currentList[adapterPosition].orderAndSubscriber.order.status =
                    OrderStatus.Delivered
                setOrderStatus(OrderStatus.Delivered)
            }
        }
    }

    fun setOrderDayId(dayId: Int) {
        binding.searchOrderDayId.text = dayId.toString()
    }

    fun setOrderDate(dateTime: ZonedDateTime) {
        binding.searchOrderDate.text = dateTime.toJalaliIso()
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
        binding.searchOrderStatus.text = statusText
    }

    fun setOrderDetails(orderDetailIds: List<OrderDetail>) {
        val detail = buildString { orderDetailIds.forEach { this.append("${it.summary}\n") } }
        binding.searchOrderDetails.text = detail
    }

    fun setDescription(description: String?) {
        if (description == null) hideDescription()
        else showDescription(description)
    }

    private fun showDescription(description: String) {
        binding.searchOrderDescription.text = description
        binding.searchOrderDescriptionHolder.visible()
        binding.searchOrderDescription.visible()
    }

    private fun hideDescription() {
        binding.searchOrderDescription.gone()
        binding.searchOrderDescriptionHolder.gone()
    }

    fun setSubscriber(subscriber: Subscriber?) {
        if (subscriber == null) hideSubscriber()
        else showSubscriber(subscriber)
    }

    private fun showSubscriber(subscriber: Subscriber) {
        binding.searchOrderSubscriber.text = subscriber.toString()
        binding.searchOrderSubscriber.visible()
        binding.searchOrderSubscriberHolder.visible()
    }

    private fun hideSubscriber() {
        binding.searchOrderSubscriber.gone()
        binding.searchOrderSubscriberHolder.gone()
    }
}