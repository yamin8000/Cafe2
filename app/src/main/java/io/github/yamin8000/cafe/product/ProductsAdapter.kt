package io.github.yamin8000.cafe.product

import io.github.yamin8000.cafe.databinding.ProductItemBinding
import io.github.yamin8000.cafe.db.entities.relatives.ProductAndCategory
import io.github.yamin8000.cafe.ui.AsyncDifferHelper.getAsyncDiffer
import io.github.yamin8000.cafe.ui.recyclerview.CrudAdapter

class ProductsAdapter(
    private val updateCallback: (ProductAndCategory) -> Unit,
    private val deleteCallback: (ProductAndCategory, Boolean) -> Unit
) : CrudAdapter<ProductsHolder>() {

    val asyncList = this.getAsyncDiffer<ProductAndCategory, ProductsHolder>(
        { old, new -> old.product.id == new.product.id },
        { old, new -> old == new }
    )

    init {
        initAdapter({ parent, inflater ->
            val binding = ProductItemBinding.inflate(inflater, parent, false)
            ProductsHolder(asyncList, binding, updateCallback, deleteCallback)
        }, { holder, position ->
            holder.bind(asyncList.currentList[position])
        }, { asyncList.currentList.size })
    }
}