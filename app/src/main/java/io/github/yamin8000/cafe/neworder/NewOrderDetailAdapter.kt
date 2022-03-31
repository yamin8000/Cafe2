package io.github.yamin8000.cafe.neworder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.yamin8000.cafe.databinding.NewOrderDetailItemBinding
import io.github.yamin8000.cafe.db.entities.product.Product
import io.github.yamin8000.cafe.ui.recyclerview.AsyncDifferHelper.getAsyncDiffer

class NewOrderDetailAdapter(
    private val itemChanged: (Pair<Product, Int>) -> Unit
) : RecyclerView.Adapter<NewOrderDetailHolder>() {

    val asyncList = this.getAsyncDiffer<Product, NewOrderDetailHolder>(
        { old, new -> old.id == new.id },
        { old, new -> old == new }
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewOrderDetailHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = NewOrderDetailItemBinding.inflate(inflater, parent, false)
        return NewOrderDetailHolder(itemBinding, asyncList, itemChanged)
    }

    override fun onBindViewHolder(holder: NewOrderDetailHolder, position: Int) {
        val orderDetail = asyncList.currentList[position]
        holder.setDetailText(orderDetail.name)
        holder.setPrice(orderDetail.price)
    }

    override fun getItemCount() = asyncList.currentList.size

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position
}