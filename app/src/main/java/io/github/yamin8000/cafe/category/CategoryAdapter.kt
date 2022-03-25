package io.github.yamin8000.cafe.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.yamin8000.cafe.databinding.CategoryItemBinding
import io.github.yamin8000.cafe.db.entities.category.Category
import io.github.yamin8000.cafe.ui.AsyncDifferHelper

class CategoryAdapter(
    private val updateCallback: (Category) -> Unit,
    private val deleteCallback: (Category, Boolean) -> Unit
) : RecyclerView.Adapter<CategoryHolder>() {

    val asyncList = AsyncDifferHelper.getAsyncDiffer<Category, CategoryHolder>(this,
        { old, new -> old.id == new.id },
        { old, new -> old == new })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = CategoryItemBinding.inflate(inflater, parent, false)
        return CategoryHolder(asyncList, itemBinding, updateCallback, deleteCallback)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.initItem(asyncList.currentList[position])
    }

    override fun getItemCount() = asyncList.currentList.size

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position
}