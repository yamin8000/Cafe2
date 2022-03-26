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
    { category ->
        binding.categoryItemText.text = category.name
        val drawableId = category.imageId
        binding.categoryItemImage.setImageResource(drawableId)
    })