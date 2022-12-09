/*
 *     Cafe/Cafe.app.main
 *     NewOrderNewProductModal.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     NewOrderNewProductModal.kt Last modified at 2022/12/9
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

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.ChooseProductModalBinding
import io.github.yamin8000.cafe.db.entities.category.Category
import io.github.yamin8000.cafe.db.entities.relatives.ProductAndCategory
import io.github.yamin8000.cafe.product.ProductsAdapter
import io.github.yamin8000.cafe.ui.AutoArrayAdapter
import io.github.yamin8000.cafe.ui.SimpleDelayedTextWatcher
import io.github.yamin8000.cafe.util.Constants.DATA
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.Alerts.snack
import io.github.yamin8000.cafe.util.Utility.handleCrash
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewOrderNewProductModal : BottomSheetDialogFragment() {

    private val iosScope = CoroutineScope(Dispatchers.IO)

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ChooseProductModalBinding.inflate(LayoutInflater.from(context))
    }

    private var allProducts = listOf<ProductAndCategory>()

    private val selectedProducts = mutableSetOf<ProductAndCategory>()

    private val adapter = ProductsAdapter(false, this::onClick, this::onLongClick)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val safeContext = context
        return if (safeContext != null) {
            val dialog = BottomSheetDialog(safeContext, theme)
            dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            dialog
        } else super.onCreateDialog(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            lifecycleScope.launch {
                allProducts = fetchAllProducts()
                fillList()
            }
            lifecycleScope.launch { handleCategoriesAutoComplete() }
            productNameTextWatcherHandler()
            confirmClickListener()
            categoryClearHandler()
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    private fun categoryClearHandler() {
        binding.chooseProductCategoryInput.setStartIconOnClickListener {
            binding.chooseProductCategoryAuto.text?.clear()
            fillList()
        }
    }

    private fun productNameTextWatcherHandler() {
        binding.chooseProductNameEdit.addTextChangedListener(SimpleDelayedTextWatcher(500) {
            val name = binding.chooseProductNameEdit.text?.toString()
            if (!name.isNullOrBlank())
                fillList(name)
            else fillList()
        })
    }

    private fun confirmClickListener() {
        binding.chooseProductConfirm.setOnClickListener {
            if (selectedProducts.isNotEmpty())
                sendSelectedProducts()
            else snack(getString(R.string.no_order_registered))
        }
    }

    private fun sendSelectedProducts() {
        setFragmentResult(DATA, bundleOf(DATA to ArrayList(selectedProducts)))
        dismiss()
    }

    @Suppress("UNUSED_PARAMETER")
    private suspend fun fetchAllProducts(): List<ProductAndCategory> {
        return withContext(iosScope.coroutineContext) {
            db.relativeDao().getProductsAndCategories()
        }
    }

    private suspend fun handleCategoriesAutoComplete() {
        context?.let {
            AutoArrayAdapter(
                it,
                getCategories(),
                binding.chooseProductCategoryAuto
            ) { category, _ -> fillList(category.id) }
        }
    }

    private fun fillList() {
        adapter.asyncList.submitList(allProducts)
        binding.chooseProductList.adapter = adapter
    }

    private fun fillList(categoryId: Long) {
        adapter.asyncList.submitList(getProductsByCategory(categoryId))
        binding.chooseProductList.adapter = adapter
    }

    private fun fillList(productName: String) {
        adapter.asyncList.submitList(getProductsByName(productName))
        binding.chooseProductList.adapter = adapter
    }

    private fun getProductsByName(productName: String): List<ProductAndCategory> {
        return allProducts.filter { it.product?.name?.contains(productName, true) ?: true }
    }

    private fun getProductsByCategory(categoryId: Long): List<ProductAndCategory> {
        return allProducts.filter { it.category.id == categoryId }
    }

    private suspend fun getCategories(): List<Category> {
        return withContext(iosScope.coroutineContext) {
            db.categoryDao().getAll()
        }
    }

    private fun onClick(item: ProductAndCategory) {
        selectedProducts.add(item)
        updateChosenProductsTextView()
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onLongClick(item: ProductAndCategory, isChecked: Boolean) {
        selectedProducts.remove(item)
        updateChosenProductsTextView()
    }

    private fun updateChosenProductsTextView() {
        val products = selectedProducts.joinToString(separator = "\n") { it.product?.name ?: "" }
        val text = buildString {
            append(getString(R.string.chosen_products))
            append(":\n")
            append(products)
        }
        binding.chosenProducts.text = text
    }
}