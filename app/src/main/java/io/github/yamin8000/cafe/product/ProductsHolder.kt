/*
 *     Cafe/Cafe.app.main
 *     ProductsHolder.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     ProductsHolder.kt Last modified at 2022/12/9
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

import android.view.View
import androidx.recyclerview.widget.AsyncListDiffer
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.ProductItemBinding
import io.github.yamin8000.cafe.db.entities.relatives.ProductAndCategory
import io.github.yamin8000.cafe.ui.crud.CrudHolder
import io.github.yamin8000.cafe.util.Utility.Views.setImageFromResourceId
import io.github.yamin8000.cafe.util.Utility.numFormat

class ProductsHolder(
    isCheckable: Boolean,
    asyncList: AsyncListDiffer<ProductAndCategory>,
    binding: ProductItemBinding,
    updateCallback: (ProductAndCategory) -> Unit,
    deleteCallback: (ProductAndCategory, Boolean) -> Unit,
) : CrudHolder<ProductAndCategory, ProductItemBinding>(
    asyncList,
    binding,
    updateCallback,
    deleteCallback,
    { productAndCategory, _ ->
        productAndCategory.product?.let { product ->
            binding.productItemName.text = product.name
            binding.productItemCategory.text = productAndCategory.category.name
            binding.productItemPrice.text = binding.root.resources.getString(
                R.string.rial_template,
                product.price.toString().numFormat()
            )
            val imageId = product.imageId
            binding.productItemImage.visibility = if (imageId == null) View.GONE else View.VISIBLE
            imageId?.let { binding.productItemImage.setImageFromResourceId(it) }
        }
    }
)