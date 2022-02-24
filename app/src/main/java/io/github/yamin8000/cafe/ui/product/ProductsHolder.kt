package io.github.yamin8000.cafe.ui.product

import androidx.recyclerview.widget.RecyclerView
import io.github.yamin8000.cafe.databinding.ProductItemBinding
import io.github.yamin8000.cafe.db.product.Product

class ProductsHolder(
    private val products: MutableList<Product>,
    private val binding: ProductItemBinding,
    private val deleteListener: (Product) -> Unit,
    private val deleteNotifier: (Int) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    init {
        binding.productItemText.setOnLongClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                deleteListener(products[adapterPosition])
                products.remove(products[adapterPosition])
                deleteNotifier(adapterPosition)
            }
            return@setOnLongClickListener true
        }
    }

    fun setProductText(productText: String) {
        binding.productItemText.text = productText
    }
}