package io.github.yamin8000.cafe.product

import io.github.yamin8000.cafe.databinding.ProductItemBinding
import io.github.yamin8000.cafe.db.entities.relatives.ProductAndCategory
import io.github.yamin8000.cafe.ui.crud.CrudAdapter
import io.github.yamin8000.cafe.ui.recyclerview.AsyncDifferHelper.getAsyncDiffer

class ProductsAdapter(
    isCheckable: Boolean,
    private val updateCallback: (ProductAndCategory) -> Unit,
    private val deleteCallback: (ProductAndCategory, Boolean) -> Unit
) : CrudAdapter<ProductAndCategory, ProductsHolder>() {

    override var asyncList = this.getAsyncDiffer<ProductAndCategory, ProductsHolder>(
        { old, new -> old.product?.id == new.product?.id },
        { old, new -> old == new }
    )

    init {
        initAdapter({ parent, inflater ->
            val binding = ProductItemBinding.inflate(inflater, parent, false)
            binding.root.isCheckable = isCheckable
            ProductsHolder(isCheckable, asyncList, binding, updateCallback, deleteCallback)
        }, { holder, position ->
            holder.bind(asyncList.currentList[position])
        }, { asyncList.currentList.size })
    }
}