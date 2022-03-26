package io.github.yamin8000.cafe.ui.crud

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView

open class CrudAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    private lateinit var viewHolderCreator: (ViewGroup, LayoutInflater) -> VH
    private lateinit var viewHolderBinder: (VH, Int) -> Unit
    private lateinit var itemCountProvider: () -> Int

    open lateinit var asyncList: AsyncListDiffer<T>

    fun initAdapter(
        viewHolderCreator: (ViewGroup, LayoutInflater) -> VH,
        viewHolderBinder: (VH, Int) -> Unit,
        itemCountProvider: () -> Int
    ) {
        this.viewHolderCreator = viewHolderCreator
        this.viewHolderBinder = viewHolderBinder
        this.itemCountProvider = itemCountProvider
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return viewHolderCreator(parent, LayoutInflater.from(parent.context))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        viewHolderBinder(holder, position)
    }

    override fun getItemCount() = itemCountProvider()

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position
}