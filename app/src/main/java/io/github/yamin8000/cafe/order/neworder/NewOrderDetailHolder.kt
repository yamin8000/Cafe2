/*
 *     Cafe/Cafe.app.main
 *     NewOrderDetailHolder.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     NewOrderDetailHolder.kt Last modified at 2022/12/9
 *     This file is part of Cafe/Cafe.app.main.
 *     Copyright (C) 2022  Yamin Siahmargooei
 *
 *     Cafe/Cafe.app.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cafe/Cafe.app.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cafe.  If not, see <https://www.gnu.org/licenses/>.
 */

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