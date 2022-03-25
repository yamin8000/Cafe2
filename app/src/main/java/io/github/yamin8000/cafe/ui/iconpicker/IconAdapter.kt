package io.github.yamin8000.cafe.ui.iconpicker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.yamin8000.cafe.databinding.IconItemBinding

class IconAdapter(
    private val drawableIds: List<Int>,
    private val callback: (Int) -> Unit
) : RecyclerView.Adapter<IconHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = IconItemBinding.inflate(inflater, parent, false)
        return IconHolder(drawableIds, binding, callback)
    }

    override fun onBindViewHolder(holder: IconHolder, position: Int) {
        holder.initView(position)
    }

    override fun getItemCount() = drawableIds.size

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position
}