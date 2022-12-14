/*
 *     Cafe/Cafe.app.main
 *     FragmentProductReport.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     FragmentProductReport.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe2.report.reports

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import io.github.yamin8000.cafe2.R
import io.github.yamin8000.cafe2.databinding.FragmentProductReportBinding
import io.github.yamin8000.cafe2.db.entities.category.Category
import io.github.yamin8000.cafe2.db.entities.relatives.ProductAndCategory
import io.github.yamin8000.cafe2.product.ProductsAdapter
import io.github.yamin8000.cafe2.report.ReportCSVs
import io.github.yamin8000.cafe2.ui.AutoArrayAdapter
import io.github.yamin8000.cafe2.ui.SimpleDelayedTextWatcher
import io.github.yamin8000.cafe2.util.Constants.DATA
import io.github.yamin8000.cafe2.util.Constants.TEXT_WATCHER_DELAY
import io.github.yamin8000.cafe2.util.Constants.db
import io.github.yamin8000.cafe2.util.Utility.Views.handlePrice
import io.github.yamin8000.cafe2.util.Utility.handleCrash
import io.github.yamin8000.cafe2.util.Utility.navigate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentProductReport :
    BaseFragmentReport<ProductAndCategory, FragmentProductReportBinding>({
        FragmentProductReportBinding.inflate(it)
    }) {

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        ProductsAdapter(false, {}) { _, _ -> }
    }

    private var categoryId: Long? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            lifecycleScope.launch { handleArguments() }
            lifecycleScope.launch { handleCategoriesAutocomplete() }
            handleCategoriesAutoCompleteClearButton()
            handleTextWatchers()
            binding.productReportShare.setOnClickListener {
                shareCsv(ReportCSVs.productAndCategory(items))
            }
            binding.reportProductToolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.best_selling -> lifecycleScope.launch { showBestSellers() }
                }
                true
            }
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    private suspend fun showBestSellers() {
        val bestSellers = withContext(ioScope.coroutineContext) {
            db.orderDetailDao().getBestSelling()
        }
        navigate(R.id.simpleListModal, bundleOf(DATA to bestSellers.map { detail ->
            buildString {
                append(items.find { detail.productId == it.product?.id }?.product?.name ?: "")
                append("\n")
                append(getString(R.string.sell_quantity))
                append("\n")
                append(detail.quantity)
            }
        }.toTypedArray()))
    }

    private fun sortByBestSelling() {
        lifecycleScope.launch {
            withContext(ioScope.coroutineContext) { sortToBestSelling() }
            createList(items)
        }
    }

    private suspend fun sortToBestSelling() {
        val bestSellers = db.orderDetailDao().getBestSelling()
        val iterator = items.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            val bestSellerItem = bestSellers.find { it.productId == item.product?.id }
            if (bestSellerItem != null)
                item.product?.name = "${bestSellerItem.quantity}"
        }
    }

    private fun handleTextWatchers() {
        binding.productReportNameEdit.addTextChangedListener(
            SimpleDelayedTextWatcher(TEXT_WATCHER_DELAY) {
                lifecycleScope.launch { handleArguments() }
            })
        binding.productReportMinPriceEdit.addTextChangedListener(
            SimpleDelayedTextWatcher(TEXT_WATCHER_DELAY) {
                lifecycleScope.launch { handleArguments() }
            })
        binding.productReportMaxPriceEdit.addTextChangedListener(
            SimpleDelayedTextWatcher(TEXT_WATCHER_DELAY) {
                lifecycleScope.launch { handleArguments() }
            })
    }

    private fun handleCategoriesAutoCompleteClearButton() {
        binding.productReportCategoryInput.setStartIconOnClickListener {
            binding.productReportCategoryAuto.text?.clear()
            categoryId = null
            lifecycleScope.launch { handleArguments() }
        }
    }

    private suspend fun handleCategoriesAutocomplete() {
        val categories = getCategories()
        context?.let {
            AutoArrayAdapter(it, categories, binding.productReportCategoryAuto) { category, _ ->
                categoryId = category.id
                lifecycleScope.launch { handleArguments() }
            }
        }
    }

    private suspend fun getCategories(): List<Category> {
        return db.categoryDao().getAll()
    }

    override suspend fun getItems(): List<ProductAndCategory> {
        return db.relativeDao().getProductsAndCategories()
    }

    override suspend fun filterItems(items: List<ProductAndCategory>): List<ProductAndCategory> {
        val minPrice = binding.productReportMinPriceEdit.handlePrice()
        val maxPrice = binding.productReportMaxPriceEdit.handlePrice()
        val productName = binding.productReportNameEdit.text?.toString()

        var products = items.asSequence()
        if (categoryId != null)
            products = products.filter { it.product?.categoryId == categoryId }
        if (!productName.isNullOrBlank())
            products = products.filter { it.product?.name?.contains(productName, true) ?: true }
        if (minPrice != null)
            products = products.filter { it.product?.price ?: Long.MAX_VALUE >= minPrice }
        if (maxPrice != null)
            products = products.filter { it.product?.price ?: Long.MIN_VALUE <= maxPrice }

        return products.toList()
    }

    override fun createList(items: List<ProductAndCategory>) {
        if (items.isEmpty()) binding.productReportList.adapter = emptyAdapter
        else binding.productReportList.adapter = adapter.apply { asyncList.submitList(items) }
    }
}