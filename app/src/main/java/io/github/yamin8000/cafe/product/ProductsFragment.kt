/*
 *     Cafe/Cafe.app.main
 *     ProductsFragment.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     ProductsFragment.kt Last modified at 2022/12/9
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

import androidx.recyclerview.widget.GridLayoutManager
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.db.entities.product.Product
import io.github.yamin8000.cafe.db.entities.relatives.ProductAndCategory
import io.github.yamin8000.cafe.ui.crud.ReadDeleteFragment
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.Alerts.snack

class ProductsFragment :
    ReadDeleteFragment<ProductAndCategory, ProductsHolder>(R.id.newProductFragment) {

    override suspend fun getItems(): List<ProductAndCategory> {
        return db.relativeDao().getProductsAndCategories()
    }

    override suspend fun dbDeleteAction() {
        deleteCandidates.forEach {
            val product = it.product
            if (product != null) deleteProduct(product)
        }
        snack(getString(R.string.item_delete_success, getString(R.string.product)))
    }

    private suspend fun deleteProduct(product: Product) {
        db.productDao().delete(product)
    }

    override fun fillList() {
        val hasProducts = items.isNotEmpty() && items.all { it.product != null }
        context?.let { binding.crudList.layoutManager = GridLayoutManager(it, 2) }
        if (hasProducts) prepareList()
        else binding.crudList.adapter = emptyAdapter
    }

    private fun prepareList() {
        val adapter = ProductsAdapter(true, this::updateCallback, this::deleteCallback)
        binding.crudList.adapter = adapter
        adapter.asyncList.submitList(items)
    }
}