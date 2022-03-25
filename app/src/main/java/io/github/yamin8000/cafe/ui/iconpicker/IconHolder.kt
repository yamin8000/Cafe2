package io.github.yamin8000.cafe.ui.iconpicker

import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import io.github.yamin8000.cafe.databinding.IconItemBinding

class IconHolder(
    private val drawableIds: List<Int>,
    private val binding: IconItemBinding,
    private val callback: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION)
                callback(drawableIds[adapterPosition])
        }
    }

    fun initView(position: Int) {
        binding.drawableItemImage.setImageDrawable(
            ResourcesCompat.getDrawable(
                binding.root.resources,
                drawableIds[position],
                null
            )
        )
    }
}