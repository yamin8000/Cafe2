/*
 *     Cafe/Cafe.app.main
 *     NewCategoryFragment.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     NewCategoryFragment.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe.category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentNewCategoryBinding
import io.github.yamin8000.cafe.db.entities.category.Category
import io.github.yamin8000.cafe.ui.crud.CreateUpdateFragment
import io.github.yamin8000.cafe.util.Constants.ICON_PICKER
import io.github.yamin8000.cafe.util.Constants.ICON_PICKER_RESULT
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.Views.setImageFromResourceId
import io.github.yamin8000.cafe.util.Utility.navigate
import kotlinx.coroutines.withContext

class NewCategoryFragment :
    CreateUpdateFragment<Category, FragmentNewCategoryBinding>(
        Category(),
        { FragmentNewCategoryBinding.inflate(it) }
    ) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(binding.addCategoryConfirm)
        binding.categoryImageButton.setOnClickListener { showIconPicker() }
    }

    override fun initViewForCreateMode() {
        //ignored
    }

    override fun initViewForEditMode() {
        if (item.isCreated()) {
            binding.categoryNameEdit.setText(item.name)
            binding.categoryImage.setImageFromResourceId(item.imageId)
        }
    }

    override suspend fun createItem() {
        withContext(ioScope.coroutineContext) { db.categoryDao().insert(item) }
        addSuccess(getString(R.string.category))
    }

    override suspend fun editItem() {
        if (item.isCreated()) {
            withContext(ioScope.coroutineContext) { db.categoryDao().update(item) }
            editSuccess(getString(R.string.category))
        }
    }

    override fun validator(): Boolean {
        return item.name.isNotBlank()
    }

    override fun confirm() {
        binding.addCategoryConfirm.setOnClickListener {
            val categoryName = binding.categoryNameEdit.text.toString()
            if (item.imageId == 0)
                item.imageId = R.drawable.pack_top_view_coffee
            item.name = categoryName
            confirmItem()
        }
    }

    private fun showIconPicker() {
        navigate(R.id.action_newCategoryFragment_to_iconPickerModal)
        setFragmentResultListener(ICON_PICKER) { _, bundle ->
            item.imageId = bundle.getInt(ICON_PICKER_RESULT)
            binding.categoryImage.setImageFromResourceId(item.imageId)
        }
    }

    override fun resetViews() {
        binding.categoryImage.setImageDrawable(null)
        binding.categoryNameEdit.text?.clear()
    }
}
