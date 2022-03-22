package io.github.yamin8000.cafe.ui.searchorder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.yamin8000.cafe.databinding.SearchOrderItemBinding
import io.github.yamin8000.cafe.db.entities.order.Order
import io.github.yamin8000.cafe.db.entities.order.OrderDetail

class SearchOrderAdapter(
    private val orders: List<Order>,
    private val orderDetails: List<OrderDetail>,
    private val deliverListener: (Int) -> Unit
) : RecyclerView.Adapter<SearchOrderHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchOrderHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = SearchOrderItemBinding.inflate(inflater, parent, false)
        return SearchOrderHolder(itemBinding, orders, orderDetails, deliverListener)
    }

    override fun onBindViewHolder(holder: SearchOrderHolder, position: Int) {
        val model = orders[position]

        holder.setOrderDate(model.date)
        holder.setOrderDayId(model.dayId)
        holder.setOrderStatus(model.status)
        holder.setOrderDetails(model.detailIds)
    }

    override fun getItemCount() = orders.size

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position
}
