package io.github.yamin8000.cafe.ui.recyclerview

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.google.android.material.card.MaterialCardView

open class CrudHolder<T, VB : ViewBinding>(
    private val asyncList: AsyncListDiffer<T>,
    protected val binding: VB,
    protected val updateCallback: (T) -> Unit,
    protected val deleteCallback: (T, Boolean) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val card = binding.root as MaterialCardView

    init {
        binding.root.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                updateCallback(asyncList.currentList[adapterPosition])
            }
        }
        binding.root.setOnLongClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                card.isChecked = !card.isChecked
                deleteCallback(asyncList.currentList[adapterPosition], card.isChecked)
            }
            true
        }
    }
}