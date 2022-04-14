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
        return db.relativeDao().getProductsAndCategories() ?: listOf()
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
        val adapter = ProductsAdapter(this::updateCallback, this::deleteCallback)
        binding.crudList.adapter = adapter
        adapter.asyncList.submitList(items)
    }
}