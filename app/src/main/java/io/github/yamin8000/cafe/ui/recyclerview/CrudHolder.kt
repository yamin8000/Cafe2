package io.github.yamin8000.cafe.ui.recyclerview

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.google.android.material.card.MaterialCardView

open class CrudHolder<T, VB : ViewBinding>(
    private val asyncList: AsyncListDiffer<T>,
    protected val binding: VB,
    protected val updateCallback: (T) -> Unit,
    protected val deleteCallback: (T, Boolean) -> Unit,
    private val bindView: (T) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val card = binding.root as MaterialCardView

    init {
        requireCheckable()
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.root.setOnClickListener { shortClickListener() }
        binding.root.setOnLongClickListener { longClickListener() }
    }

    private fun shortClickListener() {
        if (adapterPosition != RecyclerView.NO_POSITION && !card.isChecked) {
            updateCallback(asyncList.currentList[adapterPosition])
        }
    }

    private fun longClickListener(): Boolean {
        if (adapterPosition != RecyclerView.NO_POSITION) {
            card.isChecked = !card.isChecked
            deleteCallback(asyncList.currentList[adapterPosition], card.isChecked)
        }
        return true
    }

    private fun requireCheckable() {
        if (!card.isCheckable)
            card.isCheckable = true
    }

    fun bind(item: T) {
        bindView(item)
    }
}