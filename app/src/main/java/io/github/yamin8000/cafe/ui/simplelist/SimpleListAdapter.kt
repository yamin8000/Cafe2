package io.github.yamin8000.cafe.ui.simplelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.yamin8000.cafe.databinding.SimpleListItemBinding

class SimpleListAdapter(
    private val items: List<String>,
    private val itemClickListener: ((String, Int) -> Unit)? = null
) : RecyclerView.Adapter<SimpleListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleListHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SimpleListItemBinding.inflate(inflater, parent, false)
        return SimpleListHolder(items, binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: SimpleListHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}