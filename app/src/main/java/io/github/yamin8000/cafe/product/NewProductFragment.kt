package io.github.yamin8000.cafe.product

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentNewProductBinding
import io.github.yamin8000.cafe.db.entities.category.Category
import io.github.yamin8000.cafe.db.entities.product.Product
import io.github.yamin8000.cafe.db.entities.relatives.ProductAndCategory
import io.github.yamin8000.cafe.db.helpers.DbHelpers.getCategories
import io.github.yamin8000.cafe.ui.util.BaseFragment
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.Alerts.snack
import io.github.yamin8000.cafe.util.Utility.Alerts.toast
import io.github.yamin8000.cafe.util.Utility.Bundles.data
import io.github.yamin8000.cafe.util.Utility.Bundles.isEditMode
import io.github.yamin8000.cafe.util.Utility.Views.setImageFromResourceId
import io.github.yamin8000.cafe.util.Utility.handleCrash
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewProductFragment :
    BaseFragment<FragmentNewProductBinding>({ FragmentNewProductBinding.inflate(it) }) {

    private val ioScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.IO) }
    private val mainScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.Main) }

    private var productName = ""
    private var productPrice = -1L
    private var productCategory = -1L
    private var productImage: Int? = null

    private var editedProduct: ProductAndCategory? = null
    private var isEditMode = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            isEditMode = arguments.isEditMode()
            editedProduct = arguments.data()
            if (isEditMode) initEditMode()
            mainScope.launch { handleCategoriesAutoComplete() }
            binding.addProductConfirm.setOnClickListener { confirmClickListener() }
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    private fun initEditMode() {
        editedProduct?.product?.let {
            productName = it.name
            productPrice = it.price
            productCategory = it.id
            productImage = it.imageId

            binding.productNameEdit.setText(it.name)
            binding.productPriceEdit.setText(it.price.toString())
            binding.productCategoryEdit.setText(editedProduct?.category?.name)
            it.imageId?.let { imageId ->
                binding.productImage.setImageFromResourceId(imageId)
            }
        }
    }

    private fun confirmClickListener() {
        productName = binding.productNameEdit.text.toString()
        productPrice = getPrice()
        if (isParamsValid(productName, productPrice, productCategory))
            addNewProductToDb(Product(productName, productPrice, productCategory, productImage))
        else snack(getString(R.string.enter_all_fields))
    }

    private fun getPrice(): Long {
        val text = binding.productPriceEdit.text.toString()
        return if (text.isNotBlank()) text.toLong() else -1L
    }

    private suspend fun handleCategoriesAutoComplete() {
        val categories = ioScope.coroutineContext.getCategories()
        if (categories.isNotEmpty())
            fillCategoriesAutoComplete(categories)
        else toast(getString(R.string.not_categories_should_add_category))
    }

    private fun fillCategoriesAutoComplete(categories: List<Category>) {
        context?.let {
            val adapter = ArrayAdapter(it, R.layout.dropdown_item, categories)
            binding.productCategoryEdit.setAdapter(adapter)
            binding.productCategoryEdit.setOnItemClickListener { adapterView, _, position, _ ->
                val category = adapterView.getItemAtPosition(position) as Category
                productCategory = category.id
            }

        }
    }

    private fun addNewProductToDb(product: Product) {
        ioScope.launch {
            db?.productDao()?.insertAll(product)
            withContext(mainScope.coroutineContext) {
                snack(getString(R.string.item_add_success, getString(R.string.product)))
                clearProductValues()
                clearViews()
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
        productName = ""
        productPrice = -1
        productCategory = -1
        productImage = null
    }

    private fun isParamsValid(
        productName: String,
        productPrice: Long,
        productCategory: Long
    ): Boolean {
        return productName.isNotBlank() && productPrice != -1L && productCategory != -1L
    }
}