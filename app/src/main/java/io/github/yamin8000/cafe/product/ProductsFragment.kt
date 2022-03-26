package io.github.yamin8000.cafe.product

import androidx.recyclerview.widget.LinearLayoutManager
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.db.entities.relatives.ProductAndCategory
import io.github.yamin8000.cafe.ui.crud.CrudFragment
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.Alerts.snack

class ProductsFragment : CrudFragment<ProductAndCategory, ProductsHolder>(R.id.newProductFragment) {

    override suspend fun getItems(): List<ProductAndCategory> {
        val list = db?.relativeDao()?.allProductsAndCategories() ?: listOf()
        return list
    }

    override suspend fun dbDeleteAction() {
        deleteCandidates.forEach {
            val productId = it.product?.id
            if (productId != null) deleteProduct(productId)
        }
        snack(getString(R.string.item_delete_success, getString(R.string.product)))
    }

    private suspend fun deleteProduct(productId: Long) {
        db?.productDao()?.deleteById(productId)
    }

    override fun fillList() {
        val hasProducts = items.isNotEmpty() && items.all { it.product != null }
        binding.crudList.layoutManager = LinearLayoutManager(context)
        if (hasProducts) prepareList()
        else binding.crudList.adapter = emptyAdapter
    }

    private fun prepareList() {
        val adapter = ProductsAdapter(this::updateCallback, this::deleteCallback)
        binding.crudList.adapter = adapter
        adapter.asyncList.submitList(items)
    }
}