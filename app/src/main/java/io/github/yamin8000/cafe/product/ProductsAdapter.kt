/*
 *     Cafe/Cafe.app.main
 *     ProductsAdapter.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     ProductsAdapter.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe.product

import io.github.yamin8000.cafe.databinding.ProductItemBinding
import io.github.yamin8000.cafe.db.entities.relatives.ProductAndCategory
import io.github.yamin8000.cafe.ui.crud.CrudAdapter
import io.github.yamin8000.cafe.ui.recyclerview.AsyncDifferHelper.getAsyncDiffer

class ProductsAdapter(
    isCheckable: Boolean,
    private val updateCallback: (ProductAndCategory) -> Unit,
    private val deleteCallback: (ProductAndCategory, Boolean) -> Unit
) : CrudAdapter<ProductAndCategory, ProductsHolder>() {

    override var asyncList = this.getAsyncDiffer<ProductAndCategory, ProductsHolder>(
        { old, new -> old.product?.id == new.product?.id },
        { old, new -> old == new }
    )

    init {
        initAdapter({ parent, inflater ->
            val binding = ProductItemBinding.inflate(inflater, parent, false)
            binding.root.isCheckable = isCheckable
            ProductsHolder(isCheckable, asyncList, binding, updateCallback, deleteCallback)
        }, { holder, position ->
            holder.bind(asyncList.currentList[position])
        }, { asyncList.currentList.size })
    }
}