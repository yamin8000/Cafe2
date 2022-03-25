package io.github.yamin8000.cafe.ui

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

object AsyncDifferHelper {

    fun <T, VH : RecyclerView.ViewHolder> RecyclerView.Adapter<VH>.getAsyncDiffer(
        areItemTheSame: (oldItem: T, newItem: T) -> Boolean,
        areContentsTheSame: (oldItem: T, newItem: T) -> Boolean
    ): AsyncListDiffer<T> {
        return AsyncListDiffer(this, getDiffCallback(areItemTheSame, areContentsTheSame))
    }

    private fun <T> getDiffCallback(
        areItemTheSame: (oldItem: T, newItem: T) -> Boolean,
        areContentsTheSame: (oldItem: T, newItem: T) -> Boolean
    ): DiffUtil.ItemCallback<T> {
        return object : DiffUtil.ItemCallback<T>() {
            override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
                return areItemTheSame(oldItem, newItem)
            }

            override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
                return areContentsTheSame(oldItem, newItem)
            }
        }
    }
}