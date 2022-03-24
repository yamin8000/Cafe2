package io.github.yamin8000.cafe.neworder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.yamin8000.cafe.databinding.NewOrderDetailItemBinding
import io.github.yamin8000.cafe.db.entities.product.Product

class NewOrderDetailAdapter(
    private val orderDetails: List<Product>,
    private val itemChanged: (Pair<Int, Int>) -> Unit
) : RecyclerView.Adapter<NewOrderDetailHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewOrderDetailHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = NewOrderDetailItemBinding.inflate(inflater, parent, false)
        return NewOrderDetailHolder(itemBinding, orderDetails, itemChanged)
    }

    override fun onBindViewHolder(holder: NewOrderDetailHolder, position: Int) {
        val orderDetail = orderDetails[position]
        holder.setDetailText(orderDetail.name)
    }

    override fun getItemCount() = orderDetails.size

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position
}