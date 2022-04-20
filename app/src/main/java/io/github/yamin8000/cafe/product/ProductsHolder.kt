package io.github.yamin8000.cafe.product

import android.view.View
import androidx.recyclerview.widget.AsyncListDiffer
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.ProductItemBinding
import io.github.yamin8000.cafe.db.entities.relatives.ProductAndCategory
import io.github.yamin8000.cafe.ui.crud.CrudHolder
import io.github.yamin8000.cafe.util.Utility.Views.setImageFromResourceId
import io.github.yamin8000.cafe.util.Utility.numFormat

class ProductsHolder(
    isCheckable: Boolean,
    asyncList: AsyncListDiffer<ProductAndCategory>,
    binding: ProductItemBinding,
    updateCallback: (ProductAndCategory) -> Unit,
    deleteCallback: (ProductAndCategory, Boolean) -> Unit,
) : CrudHolder<ProductAndCategory, ProductItemBinding>(
    asyncList,
    binding,
    updateCallback,
    deleteCallback,
    { productAndCategory, _ ->
        productAndCategory.product?.let { product ->
            binding.productItemName.text = product.name
            binding.productItemCategory.text = productAndCategory.category.name
            binding.productItemPrice.text = binding.root.resources.getString(
                R.string.rial_template,
                product.price.toString().numFormat()
            )
            val imageId = product.imageId
            binding.productItemImage.visibility = if (imageId == null) View.GONE else View.VISIBLE
            imageId?.let { binding.productItemImage.setImageFromResourceId(it) }
        }
    }
)