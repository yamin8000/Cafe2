package io.github.yamin8000.cafe.report.reports

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import io.github.yamin8000.cafe.databinding.FragmentProductReportBinding
import io.github.yamin8000.cafe.db.entities.category.Category
import io.github.yamin8000.cafe.db.entities.relatives.ProductAndCategory
import io.github.yamin8000.cafe.product.ProductsAdapter
import io.github.yamin8000.cafe.ui.AutoArrayAdapter
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.Views.handlePrice
import io.github.yamin8000.cafe.util.Utility.handleCrash
import kotlinx.coroutines.launch

class FragmentProductReport :
    BaseFragmentReport<ProductAndCategory, FragmentProductReportBinding>({
        FragmentProductReportBinding.inflate(it)
    }) {

    private val adapter by lazy(LazyThreadSafetyMode.NONE) { ProductsAdapter({}) { _, _ -> } }

    private var categoryId: Long? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            lifecycleScope.launch { handleArguments() }
            binding.productOrderSearch.setOnClickListener {
                lifecycleScope.launch { handleArguments() }
            }
            lifecycleScope.launch { handleCategoriesAutocomplete() }
            handleCategoriesAutoCompleteClearButton()
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    private fun handleCategoriesAutoCompleteClearButton() {
        binding.productReportCategoryInput.setStartIconOnClickListener {
            binding.productReportCategoryAuto.text?.clear()
            categoryId = null
        }
    }

    private suspend fun handleCategoriesAutocomplete() {
        val categories = getCategories()
        context?.let {
            AutoArrayAdapter(it, categories, binding.productReportCategoryAuto) { category, _ ->
                categoryId = category.id
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

        var products = items
        if (categoryId != null)
            products = products.filter { it.product?.categoryId == categoryId }
        if (!productName.isNullOrBlank())
            products = products.filter { it.product?.name?.contains(productName, true) ?: true }
        if (minPrice != null)
            products = products.filter { it.product?.price ?: Long.MAX_VALUE >= minPrice }
        if (maxPrice != null)
            products = products.filter { it.product?.price ?: Long.MIN_VALUE <= maxPrice }

        return products
    }

    override fun createList(items: List<ProductAndCategory>) {
        if (items.isEmpty()) binding.productReportList.adapter = emptyAdapter
        else binding.productReportList.adapter = adapter.apply { asyncList.submitList(items) }
    }
}