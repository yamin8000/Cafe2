package io.github.yamin8000.cafe.ui.drawable

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.yamin8000.cafe.databinding.DrawableItemBinding

class DrawableAdapter(
    private val drawables: List<Pair<Int, Drawable?>>,
    private val callback: (Int, Drawable) -> Unit
) : RecyclerView.Adapter<DrawableHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawableHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DrawableItemBinding.inflate(inflater, parent, false)
        return DrawableHolder(drawables, binding, callback)
    }

    override fun onBindViewHolder(holder: DrawableHolder, position: Int) {
        holder.initView(position)
    }

    override fun getItemCount() = drawables.size

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position
}