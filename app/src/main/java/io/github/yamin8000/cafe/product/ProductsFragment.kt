package io.github.yamin8000.cafe.product

import androidx.recyclerview.widget.LinearLayoutManager
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.db.entities.relatives.ProductAndCategory
import io.github.yamin8000.cafe.ui.CrudFragment
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.Alerts.snack

class ProductsFragment : CrudFragment<ProductAndCategory, ProductsHolder>(R.id.newProductFragment) {

    override suspend fun getItems(): List<ProductAndCategory> {
        return db?.relativeDao()?.allProductsAndCategories() ?: listOf()
    }

    override suspend fun dbDeleteAction() {
        deleteCandidates.forEach { db?.productDao()?.deleteById(it.product.id) }
        snack(getString(R.string.item_delete_success, getString(R.string.product)))
    }

    override fun fillList() {
        val adapter = ProductsAdapter(this::updateCallback, this::deleteCallback)
        binding.crudList.adapter = adapter
        binding.crudList.layoutManager = LinearLayoutManager(context)
        adapter.asyncList.submitList(items)
    }
}