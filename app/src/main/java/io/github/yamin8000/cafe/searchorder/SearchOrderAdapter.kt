package io.github.yamin8000.cafe.searchorder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.yamin8000.cafe.databinding.SearchOrderItemBinding
import io.github.yamin8000.cafe.db.entities.relatives.OrderWithDetails

class SearchOrderAdapter(
    private val orders: List<OrderWithDetails>,
    private val deliverListener: (Long) -> Unit
) : RecyclerView.Adapter<SearchOrderHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchOrderHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = SearchOrderItemBinding.inflate(inflater, parent, false)
        return SearchOrderHolder(itemBinding, orders, deliverListener)
    }

    override fun onBindViewHolder(holder: SearchOrderHolder, position: Int) {
        val model = orders[position]

        holder.setOrderDate(model.orderAndSubscriber.order.date)
        holder.setOrderDayId(model.orderAndSubscriber.order.dayId)
        holder.setOrderStatus(model.orderAndSubscriber.order.status)
        holder.setOrderDetails(model.orderDetails)
        holder.setDescription(model.orderAndSubscriber.order.description)
        holder.setSubscriber(model.orderAndSubscriber.subscriber)
    }

    override fun getItemCount() = orders.size

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position
}
