package io.github.yamin8000.cafe.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.yamin8000.cafe.databinding.ProductItemBinding
import io.github.yamin8000.cafe.db.entities.relatives.ProductAndCategory
import io.github.yamin8000.cafe.ui.AsyncDifferHelper.getAsyncDiffer

class ProductsAdapter(
    private val updateCallback: (ProductAndCategory) -> Unit,
    private val deleteCallback: (ProductAndCategory, Boolean) -> Unit
) : RecyclerView.Adapter<ProductsHolder>() {

    val asyncList = this.getAsyncDiffer<ProductAndCategory, ProductsHolder>(
        { old, new -> old.product.id == new.product.id },
        { old, new -> old == new }
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ProductItemBinding.inflate(inflater, parent, false)
        return ProductsHolder(asyncList, itemBinding, updateCallback, deleteCallback)
    }

    override fun onBindViewHolder(holder: ProductsHolder, position: Int) {
        holder.bindView(asyncList.currentList[position])
    }

    override fun getItemCount() = asyncList.currentList.size

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position
}