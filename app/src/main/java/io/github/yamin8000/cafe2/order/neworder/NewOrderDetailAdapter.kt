/*
 *     Cafe/Cafe.app.main
 *     NewOrderDetailAdapter.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     NewOrderDetailAdapter.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe2.order.neworder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.yamin8000.cafe2.databinding.NewOrderBasketItemBinding
import io.github.yamin8000.cafe2.db.entities.product.Product
import io.github.yamin8000.cafe2.ui.recyclerview.AsyncDifferHelper.getAsyncDiffer

class NewOrderDetailAdapter(
    private val itemChanged: (Pair<Product, Int>) -> Unit
) : RecyclerView.Adapter<NewOrderDetailHolder>() {

    val asyncList = this.getAsyncDiffer<Product, NewOrderDetailHolder>(
        { old, new -> old.id == new.id },
        { old, new -> old == new }
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewOrderDetailHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = NewOrderBasketItemBinding.inflate(inflater, parent, false)
        return NewOrderDetailHolder(itemBinding, asyncList, itemChanged)
    }

    override fun onBindViewHolder(holder: NewOrderDetailHolder, position: Int) {
        val product = asyncList.currentList[position]
        holder.setName(product.name)
    }

    override fun getItemCount() = asyncList.currentList.size

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position
}