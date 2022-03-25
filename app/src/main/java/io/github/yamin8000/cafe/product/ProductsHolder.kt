package io.github.yamin8000.cafe.product

import androidx.recyclerview.widget.AsyncListDiffer
import io.github.yamin8000.cafe.ui.recyclerview.CrudHolder
import io.github.yamin8000.cafe.databinding.ProductItemBinding
import io.github.yamin8000.cafe.db.entities.relatives.ProductAndCategory

class ProductsHolder(
    asyncList: AsyncListDiffer<ProductAndCategory>,
    binding: ProductItemBinding,
    updateCallback: (ProductAndCategory) -> Unit,
    deleteCallback: (ProductAndCategory, Boolean) -> Unit,
) : CrudHolder<ProductAndCategory, ProductItemBinding>(
    asyncList,
    binding,
    updateCallback,
    deleteCallback
) {
    fun bindView(productAndCategory: ProductAndCategory) {
        binding.productItemName.text = productAndCategory.product.name
        binding.productItemCategory.text = productAndCategory.category.name
        binding.productItemPrice.text = productAndCategory.product.price.toString()
    }
}