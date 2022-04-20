package io.github.yamin8000.cafe.category

import androidx.recyclerview.widget.GridLayoutManager
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.db.entities.category.Category
import io.github.yamin8000.cafe.ui.crud.ReadDeleteFragment
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.Alerts.snack
import kotlinx.coroutines.withContext

class CategoryFragment : ReadDeleteFragment<Category, CategoryHolder>(R.id.newCategoryFragment) {

    override suspend fun getItems(): List<Category> {
        return withContext(ioScope.coroutineContext) {
            db.categoryDao().getAll()
        }
    }

    override suspend fun dbDeleteAction() {
        db.categoryDao().deleteAll(deleteCandidates)
        snack(getString(R.string.item_delete_success, getString(R.string.category)))
    }

    override fun fillList() {
        val adapter = CategoryAdapter(this::updateCallback, this::deleteCallback)
        binding.crudList.adapter = adapter
        context.let { binding.crudList.layoutManager = GridLayoutManager(it, 2) }
        adapter.asyncList.submitList(items)
    }
}
