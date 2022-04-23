package io.github.yamin8000.cafe.ui.simplelist

import androidx.recyclerview.widget.RecyclerView
import io.github.yamin8000.cafe.databinding.SimpleListItemBinding

class SimpleListHolder(
    private val items: List<String>,
    private val binding: SimpleListItemBinding,
    private val itemClickListener: ((String, Int) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION)
                itemClickListener?.invoke(items[adapterPosition], adapterPosition)
        }
    }

    fun bind(item: String) {
        binding.simpleItemText.text = item
    }
}