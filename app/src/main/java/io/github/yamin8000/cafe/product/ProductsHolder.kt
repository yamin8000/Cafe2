package io.github.yamin8000.cafe.product

import androidx.recyclerview.widget.RecyclerView
import io.github.yamin8000.cafe.databinding.ProductItemBinding
import io.github.yamin8000.cafe.db.entities.relatives.ProductAndCategory

class ProductsHolder(
    private val productsWithCategory: MutableList<ProductAndCategory>,
    private val binding: ProductItemBinding,
    private val deleteListener: (ProductAndCategory) -> Unit,
    private val deleteNotifier: (Int) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    init {
        binding.productItemName.setOnLongClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                deleteListener(productsWithCategory[adapterPosition])
                productsWithCategory.remove(productsWithCategory[adapterPosition])
                deleteNotifier(adapterPosition)
            }
            return@setOnLongClickListener true
        }
    }

    fun bindView(productAndCategory: ProductAndCategory) {
        binding.productItemName.text = productAndCategory.product.name
        binding.productItemCategory.text = productAndCategory.category.name
        binding.productItemPrice.text = productAndCategory.product.price.toString()
    }
}