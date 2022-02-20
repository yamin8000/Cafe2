package io.github.yamin8000.cafe.ui.neworder

import androidx.recyclerview.widget.RecyclerView
import io.github.yamin8000.cafe.databinding.NewOrderDetailItemBinding
import io.github.yamin8000.cafe.db.product.Product

class NewOrderDetailHolder(
    private val binding: NewOrderDetailItemBinding,
    private val orderDetails: List<Product>,
    private val itemChanged: (Pair<Int, Int>) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var quantity = 0

    init {
        updateQuantity()

        binding.plusButton.setOnClickListener {
            quantity++
            itemChanged(orderDetails[adapterPosition].id to quantity)
            updateQuantity()
        }
        binding.minusButton.setOnClickListener {
            if (quantity != 0) {
                quantity--
                itemChanged(orderDetails[adapterPosition].id to quantity)
                updateQuantity()
            }
        }
    }

    private fun updateQuantity() {
        binding.orderQuantity.text = quantity.toString()
    }

    fun setDetailText(value: String) {
        binding.orderDetailText.text = value
    }
}