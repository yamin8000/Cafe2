/*
 *     Cafe/Cafe.app.main
 *     CategoryFragment.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     CategoryFragment.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe2.category

import androidx.recyclerview.widget.GridLayoutManager
import io.github.yamin8000.cafe2.R
import io.github.yamin8000.cafe2.db.entities.category.Category
import io.github.yamin8000.cafe2.ui.crud.ReadDeleteFragment
import io.github.yamin8000.cafe2.util.Constants.db
import io.github.yamin8000.cafe2.util.Utility.Alerts.snack
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
