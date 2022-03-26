package io.github.yamin8000.cafe.product

import androidx.recyclerview.widget.AsyncListDiffer
import io.github.yamin8000.cafe.databinding.ProductItemBinding
import io.github.yamin8000.cafe.db.entities.relatives.ProductAndCategory
import io.github.yamin8000.cafe.ui.crud.CrudHolder

class ProductsHolder(
    asyncList: AsyncListDiffer<ProductAndCategory>,
    binding: ProductItemBinding,
    updateCallback: (ProductAndCategory) -> Unit,
    deleteCallback: (ProductAndCategory, Boolean) -> Unit,
) : CrudHolder<ProductAndCategory, ProductItemBinding>(
    asyncList,
    binding,
    updateCallback,
    deleteCallback,
    { productAndCategory ->
        productAndCategory.product?.let {
            binding.productItemName.text = it.name
            binding.productItemCategory.text = productAndCategory.category.name
            binding.productItemPrice.text = it.price.toString()
        }
    }
)