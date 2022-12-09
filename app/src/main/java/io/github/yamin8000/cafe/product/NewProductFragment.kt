/*
 *     Cafe/Cafe.app.main
 *     NewProductFragment.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     NewProductFragment.kt Last modified at 2022/12/9
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

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.setFragmentResultListener
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentNewProductBinding
import io.github.yamin8000.cafe.db.entities.category.Category
import io.github.yamin8000.cafe.db.entities.product.Product
import io.github.yamin8000.cafe.db.entities.relatives.ProductAndCategory
import io.github.yamin8000.cafe.ui.crud.CreateUpdateFragment
import io.github.yamin8000.cafe.util.Constants.ICON_PICKER
import io.github.yamin8000.cafe.util.Constants.ICON_PICKER_RESULT
import io.github.yamin8000.cafe.util.Constants.NOT_CREATED_ID
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.Alerts.snack
import io.github.yamin8000.cafe.util.Utility.Views.getNumber
import io.github.yamin8000.cafe.util.Utility.Views.setImageFromResourceId
import io.github.yamin8000.cafe.util.Utility.navigate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewProductFragment :
    CreateUpdateFragment<ProductAndCategory, FragmentNewProductBinding>(
        ProductAndCategory(Product(), Category()),
        { FragmentNewProductBinding.inflate(it) }
    ) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(binding.addProductConfirm)
        lifecycleScope.launch { handleCategoriesAutoComplete() }
        binding.productImageButton.setOnClickListener { showIconPicker() }
    }

    override fun initViewForCreateMode() {
        //ignored
    }

    override fun initViewForEditMode() {
        binding.productNameEdit.setText(item.product?.name)
        binding.productPriceEdit.setText(item.product?.price.toString())
        binding.productCategoryEdit.setText(item.category.name)
        item.product?.imageId?.let { imageId ->
            binding.productImage.setImageFromResourceId(imageId)
        }
    }

    override suspend fun createItem() {
        item.product?.let { product ->
            withContext(ioScope.coroutineContext) { db.productDao().insert(product) }
            addSuccess(getString(R.string.product))
        }
    }

    override suspend fun editItem() {
        item.product?.let { product ->
            if (item.product?.isCreated() == true) {
                db.productDao().update(product)
                editSuccess(getString(R.string.product))
            }
        }
    }

    override fun validator(): Boolean {
        val isNameNotBlank = item.product?.name?.isNotBlank() ?: false
        val isPriceSet = item.product?.price != -1L
        val isCategorySet = item.category.id != NOT_CREATED_ID
        return isNameNotBlank && isPriceSet && isCategorySet
    }

    override fun confirm() {
        binding.addProductConfirm.setOnClickListener {
            item.product?.name = binding.productNameEdit.text.toString()
            item.product?.price = binding.productPriceEdit.getNumber()
            //item.categoryId, already set using auto complete click listener
            //item.imageId, already set using image picker click listener
            confirmItem()
        }
    }

    private fun showIconPicker() {
        navigate(R.id.action_newProductFragment_to_iconPickerModal)
        setFragmentResultListener(ICON_PICKER) { _, bundle ->
            item.product?.imageId = bundle.getInt(ICON_PICKER_RESULT)
            item.product?.imageId?.let { binding.productImage.setImageFromResourceId(it) }
        }
    }

    private suspend fun handleCategoriesAutoComplete() {
        val categories = withContext(ioScope.coroutineContext) {
            db.categoryDao().getAll()
        }
        if (categories.isNotEmpty())
            fillCategoriesAutoComplete(categories)
        else handleEmptyCategories()
    }

    private fun handleEmptyCategories() {
        snack(getString(R.string.not_categories_should_add_category))
        binding.productCategoryInput.isEnabled = false
        binding.addProductConfirm.isEnabled = false
    }

    private fun fillCategoriesAutoComplete(categories: List<Category>) {
        context?.let {
            val adapter = ArrayAdapter(it, R.layout.dropdown_item, categories)
            binding.productCategoryEdit.setAdapter(adapter)
            binding.productCategoryEdit.setOnItemClickListener { adapterView, _, position, _ ->
                val category = adapterView.getItemAtPosition(position) as Category
                item.category.id = category.id
                item.product?.categoryId = category.id
                item.category.name = category.name
            }
        }
    }

    override fun resetViews() {
        binding.productCategoryEdit.text.clear()
        binding.productNameEdit.text?.clear()
        binding.productPriceEdit.text?.clear()
        binding.productImage.setImageDrawable(null)
    }

    override fun clear() {
        super.clear()
        item.product = Product()
        item.category = Category()
    }
}