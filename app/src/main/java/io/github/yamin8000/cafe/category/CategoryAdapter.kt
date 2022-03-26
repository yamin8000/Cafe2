package io.github.yamin8000.cafe.category

import io.github.yamin8000.cafe.databinding.CategoryItemBinding
import io.github.yamin8000.cafe.db.entities.category.Category
import io.github.yamin8000.cafe.ui.AsyncDifferHelper.getAsyncDiffer
import io.github.yamin8000.cafe.ui.recyclerview.CrudAdapter

class CategoryAdapter(
    private val updateCallback: (Category) -> Unit,
    private val deleteCallback: (Category, Boolean) -> Unit
) : CrudAdapter<Category, CategoryHolder>() {

    override var asyncList = this.getAsyncDiffer<Category, CategoryHolder>(
        { old, new -> old.id == new.id },
        { old, new -> old == new }
    )

    init {
        initAdapter({ parent, inflater ->
            val binding = CategoryItemBinding.inflate(inflater, parent, false)
            CategoryHolder(asyncList, binding, updateCallback, deleteCallback)
        }, { holder, position ->
            holder.bind(asyncList.currentList[position])
        }, { asyncList.currentList.size })
    }
}