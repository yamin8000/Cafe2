package io.github.yamin8000.cafe.ui.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.yamin8000.cafe.databinding.ProductItemBinding
import io.github.yamin8000.cafe.db.entities.product.Product

class ProductsAdapter(
    private val products: MutableList<Product>,
    private val deleteListener: (Product) -> Unit
) :
    RecyclerView.Adapter<ProductsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ProductItemBinding.inflate(inflater, parent, false)
        return ProductsHolder(products, itemBinding, deleteListener) {
            notifyItemRemoved(it)
        }
    }

    override fun onBindViewHolder(holder: ProductsHolder, position: Int) {
        holder.setProductText(products[position].name)
    }

    override fun getItemCount() = products.size

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position
}