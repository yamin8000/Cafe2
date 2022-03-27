package io.github.yamin8000.cafe.product

import androidx.recyclerview.widget.AsyncListDiffer
import io.github.yamin8000.cafe.databinding.ProductItemBinding
import io.github.yamin8000.cafe.db.entities.relatives.ProductAndCategory
import io.github.yamin8000.cafe.ui.crud.CrudHolder
import io.github.yamin8000.cafe.util.Utility.Views.setImageFromResourceId

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
        productAndCategory.product?.let { product ->
            binding.productItemName.text = product.name
            binding.productItemCategory.text = productAndCategory.category.name
            binding.productItemPrice.text = product.price.toString()
            product.imageId?.let { imageId ->
                binding.productItemImage.setImageFromResourceId(imageId)
            }
        }
    }
)