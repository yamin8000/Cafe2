package io.github.yamin8000.cafe.product

import android.widget.ArrayAdapter
import androidx.fragment.app.setFragmentResultListener
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentNewProductBinding
import io.github.yamin8000.cafe.db.entities.category.Category
import io.github.yamin8000.cafe.db.entities.product.Product
import io.github.yamin8000.cafe.db.entities.relatives.ProductAndCategory
import io.github.yamin8000.cafe.db.helpers.DbHelpers.getCategories
import io.github.yamin8000.cafe.ui.crud.CreateUpdateFragment
import io.github.yamin8000.cafe.util.Constants.ICON_PICKER
import io.github.yamin8000.cafe.util.Constants.ICON_PICKER_RESULT
import io.github.yamin8000.cafe.util.Constants.NO_ID_LONG
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.Alerts.snack
import io.github.yamin8000.cafe.util.Utility.Views.setImageFromResourceId
import io.github.yamin8000.cafe.util.Utility.navigate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewProductFragment :
    CreateUpdateFragment<ProductAndCategory, FragmentNewProductBinding>(
        ProductAndCategory(Product("", 0L, -1L, null), Category("", -1)),
        { FragmentNewProductBinding.inflate(it) }
    ) {

    override fun init() {
        mainScope.launch { handleCategoriesAutoComplete() }
        binding.productImageButton.setOnClickListener { showIconPicker() }
    }

    override fun initViewForCreateMode() {
        //ignored
    }

    override fun initViewForEditMode() {
        binding.addProductConfirm.text = getString(R.string.edit)

        binding.productNameEdit.setText(item.product?.name)
        binding.productPriceEdit.setText(item.product?.price.toString())
        binding.productCategoryEdit.setText(item.category.name)
        item.product?.imageId?.let { imageId ->
            binding.productImage.setImageFromResourceId(imageId)
        }
    }

    override suspend fun createItem() {
        item.product?.let { product ->
            ioScope.launch {
                db?.productDao()?.insertAll(product)
                withContext(mainScope.coroutineContext) {
                    snack(getString(R.string.item_add_success, getString(R.string.product)))
                    clearProductValues()
                    clearViews()
                }
            }
        }
    }

    override suspend fun editItem() {
        item.product?.let { product ->
            if (item.product?.id != NO_ID_LONG) {
                ioScope.launch {
                    db?.productDao()?.update(product)
                    withContext(mainScope.coroutineContext) {
                        snack(getString(R.string.item_edit_success, getString(R.string.product)))
                    }
                }
            }
        }
    }

    override fun validator(): Boolean {
        val isNameNotBlank = item.product?.name?.isNotBlank() ?: false
        val isPriceSet = item.product?.price != -1L
        val isCategorySet = item.category.id != -1L
        return isNameNotBlank && isPriceSet && isCategorySet
    }

    override fun confirm() {
        binding.addProductConfirm.setOnClickListener {
            item.product?.name = binding.productNameEdit.text.toString()
            item.product?.price = getPrice()
            //item.categoryId, already set using auto complete click listener
            //item.imageId, already set using image picker click listener
            confirmListener(this::validator)
        }
    }


    private fun showIconPicker() {
        navigate(R.id.action_newProductFragment_to_iconPickerModal)
        setFragmentResultListener(ICON_PICKER) { _, bundle ->
            item.product?.imageId = bundle.getInt(ICON_PICKER_RESULT)
            item.product?.imageId?.let { binding.productImage.setImageFromResourceId(it) }
        }
    }

    private fun getPrice(): Long {
        val text = binding.productPriceEdit.text.toString()
        return if (text.isNotBlank()) text.toLong() else -1L
    }

    private suspend fun handleCategoriesAutoComplete() {
        val categories = ioScope.coroutineContext.getCategories()
        if (categories.isNotEmpty())
            fillCategoriesAutoComplete(categories)
        else snack(getString(R.string.not_categories_should_add_category))
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

    private fun clearViews() {
        binding.productCategoryEdit.text.clear()
        binding.productNameEdit.text?.clear()
        binding.productPriceEdit.text?.clear()
        binding.productImage.setImageDrawable(null)
    }

    private fun clearProductValues() {
        item.product?.name = ""
        item.product?.price = -1
        item.category.id = -1
        item.product?.imageId = null
        item.product?.id = NO_ID_LONG
    }
}