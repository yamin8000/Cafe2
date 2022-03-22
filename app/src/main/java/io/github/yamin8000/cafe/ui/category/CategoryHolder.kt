package io.github.yamin8000.cafe.ui.category

import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.CategoryItemBinding
import io.github.yamin8000.cafe.db.entities.category.Category

class CategoryHolder(
    private val categories: MutableList<Category>,
    private val binding: CategoryItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val context = binding.root.context

    private val defaultImage by lazy(LazyThreadSafetyMode.NONE) {
        AppCompatResources.getDrawable(context, R.drawable.pack_top_view_coffee)
    }

    init {

    }

    fun initItem(category: Category) {
        binding.categoryItemText.text = category.name
        val drawableId = category.imageId
        if (drawableId == null) binding.categoryItemImage.setImageDrawable(defaultImage)
        else binding.categoryItemImage.setImageResource(drawableId)
    }
}