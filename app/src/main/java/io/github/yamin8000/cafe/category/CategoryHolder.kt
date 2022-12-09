/*
 *     Cafe/Cafe.app.main
 *     CategoryHolder.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     CategoryHolder.kt Last modified at 2022/12/9
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

import androidx.recyclerview.widget.AsyncListDiffer
import io.github.yamin8000.cafe.databinding.CategoryItemBinding
import io.github.yamin8000.cafe.db.entities.category.Category
import io.github.yamin8000.cafe.ui.crud.CrudHolder

class CategoryHolder(
    asyncList: AsyncListDiffer<Category>,
    binding: CategoryItemBinding,
    updateCallback: (Category) -> Unit,
    deleteCallback: (Category, Boolean) -> Unit
) : CrudHolder<Category, CategoryItemBinding>(
    asyncList,
    binding,
    updateCallback,
    deleteCallback,
    { category, _ ->
        binding.categoryItemText.text = category.name
        val drawableId = category.imageId
        binding.categoryItemImage.setImageResource(drawableId)
    })