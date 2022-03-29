package io.github.yamin8000.cafe.neworder

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.github.yamin8000.ppn.Digits.Companion.spell
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.NewOrderDetailItemBinding
import io.github.yamin8000.cafe.db.entities.product.Product

class NewOrderDetailHolder(
    private val binding: NewOrderDetailItemBinding,
    private val asyncList: AsyncListDiffer<Product>,
    private val itemChanged: (Pair<Product, Int>) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var quantity = 0

    private var context = binding.root.context

    init {
        updateQuantity()
        orderQuantityClickListeners()
    }

    private fun orderQuantityClickListeners() {
        binding.newOrderPlusButton.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                quantity++
                itemChanged(asyncList.currentList[adapterPosition] to quantity)
                updateQuantity()
            }
        }
        binding.newOrderMinusButton.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                if (quantity != 0) {
                    quantity--
                    itemChanged(asyncList.currentList[adapterPosition] to quantity)
                    updateQuantity()
                }
            }
        }
    }

    private fun updateQuantity() {
        quantity.let {
            binding.newOrderMinusButton.isEnabled = it != 0
            binding.newOrderQuantity.text = context.getString(
                R.string.adad_template,
                it.spell()
            )
        }
    }

    fun setDetailText(value: String) {
        binding.newOrderDetailText.text = value
    }

    fun setPrice(price: Long) {
        context?.let {
            binding.newOrderItemPrice.text = it.getString(
                R.string.rial_template,
                price.spell()
            )
        }
    }
}