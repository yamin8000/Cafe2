package io.github.yamin8000.cafe.neworder

import androidx.recyclerview.widget.RecyclerView
import io.github.yamin8000.cafe.databinding.NewOrderDetailItemBinding
import io.github.yamin8000.cafe.db.entities.product.Product
import ir.yamin.digits.Digits.Companion.spell

class NewOrderDetailHolder(
    private val binding: NewOrderDetailItemBinding,
    private val orderDetails: List<Product>,
    private val itemChanged: (Pair<Int, Int>) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var quantity = 0

    init {
        updateQuantity()
        orderQuantityClickListeners()
    }

    private fun orderQuantityClickListeners() {
        binding.newOrderPlusButton.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                quantity++
                itemChanged(orderDetails[adapterPosition].id to quantity)
                updateQuantity()
            }
        }
        binding.newOrderMinusButton.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                if (quantity != 0) {
                    quantity--
                    itemChanged(orderDetails[adapterPosition].id to quantity)
                    updateQuantity()
                }
            }
        }
    }

    private fun updateQuantity() {
        val quantityString = quantity.toString()
        binding.newOrderQuantity.text = "$quantity\n${quantityString.spell()}"
    }

    fun setDetailText(value: String) {
        binding.newOrderDetailText.text = value
    }
}