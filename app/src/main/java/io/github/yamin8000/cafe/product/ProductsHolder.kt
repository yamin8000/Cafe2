package io.github.yamin8000.cafe.product

import androidx.recyclerview.widget.RecyclerView
import io.github.yamin8000.cafe.databinding.ProductItemBinding
import io.github.yamin8000.cafe.db.entities.product.Product

class ProductsHolder(
    private val products: MutableList<Product>,
    private val binding: ProductItemBinding,
    private val deleteListener: (Product) -> Unit,
    private val deleteNotifier: (Int) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    init {
        binding.productItemName.setOnLongClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                deleteListener(products[adapterPosition])
                products.remove(products[adapterPosition])
                deleteNotifier(adapterPosition)
            }
            return@setOnLongClickListener true
        }
    }

    fun setProductText(productText: String) {
        binding.productItemName.text = productText
    }
}