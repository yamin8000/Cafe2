package io.github.yamin8000.cafe.category

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import io.github.yamin8000.cafe.databinding.CategoryItemBinding
import io.github.yamin8000.cafe.db.entities.category.Category

class CategoryHolder(
    private val asyncList: AsyncListDiffer<Category>,
    private val binding: CategoryItemBinding,
    private val deleteClickListener: (Category, Boolean) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.isChecked = false
        binding.root.setOnLongClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) checkCard()
            true
        }
    }

    private fun checkCard() {
        binding.root.isChecked = !binding.root.isChecked
        deleteClickListener(asyncList.currentList[adapterPosition], binding.root.isChecked)
    }

    fun initItem(category: Category) {
        binding.categoryItemText.text = category.name
        val drawableId = category.imageId
        binding.categoryItemImage.setImageResource(drawableId)
    }
}