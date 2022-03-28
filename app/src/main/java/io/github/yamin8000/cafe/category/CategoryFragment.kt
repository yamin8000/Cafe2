package io.github.yamin8000.cafe.category

import androidx.recyclerview.widget.GridLayoutManager
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.db.entities.category.Category
import io.github.yamin8000.cafe.db.helpers.DbHelpers.getCategories
import io.github.yamin8000.cafe.ui.crud.ReadDeleteFragment
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.Alerts.snack

class CategoryFragment : ReadDeleteFragment<Category, CategoryHolder>(R.id.newCategoryFragment) {

    override suspend fun getItems(): List<Category> {
        return ioScope.coroutineContext.getCategories()
    }

    override suspend fun dbDeleteAction() {
        db?.categoryDao()?.deleteAll(deleteCandidates)
        snack(getString(R.string.item_delete_success, getString(R.string.category)))
    }

    override fun fillList() {
        val adapter = CategoryAdapter(this::updateCallback, this::deleteCallback)
        binding.crudList.adapter = adapter
        binding.crudList.layoutManager = GridLayoutManager(context, 2)
        adapter.asyncList.submitList(items)
    }
}
