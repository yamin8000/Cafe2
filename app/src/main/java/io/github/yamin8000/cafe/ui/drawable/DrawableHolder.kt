package io.github.yamin8000.cafe.ui.drawable

import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import io.github.yamin8000.cafe.databinding.DrawableItemBinding

class DrawableHolder(
    private val drawables: List<Pair<Int, Drawable?>>,
    private val binding: DrawableItemBinding,
    private val callback: (Int, Drawable) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            val pair = drawables[adapterPosition]
            val drawable = pair.second
            if (adapterPosition != RecyclerView.NO_POSITION && drawable != null)
                callback(pair.first, drawable)
        }
    }

    fun initView(position: Int) {
        binding.drawableItemImage.setImageDrawable(drawables[position].second)
    }
}