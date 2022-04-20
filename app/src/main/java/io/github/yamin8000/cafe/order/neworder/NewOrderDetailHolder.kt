package io.github.yamin8000.cafe.order.neworder

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.github.yamin8000.ppn.Digits.Companion.spell
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.NewOrderBasketItemBinding
import io.github.yamin8000.cafe.db.entities.product.Product

class NewOrderDetailHolder(
    private val binding: NewOrderBasketItemBinding,
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
        binding.basketPlus.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                quantity++
                itemChanged(asyncList.currentList[adapterPosition] to quantity)
                updateQuantity()
            }
        }
        binding.basketMinus.setOnClickListener {
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
            binding.basketMinus.isEnabled = it != 0
            binding.basketQuantity.text = context.getString(
                R.string.adad_template,
                it.spell()
            )
        }
    }

    fun setName(value: String) {
        binding.basketProductName.text = value
    }
}