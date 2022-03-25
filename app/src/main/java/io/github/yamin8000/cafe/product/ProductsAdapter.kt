package io.github.yamin8000.cafe.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.yamin8000.cafe.databinding.ProductItemBinding
import io.github.yamin8000.cafe.db.entities.relatives.ProductAndCategory

class ProductsAdapter(
    private val products: MutableList<ProductAndCategory>,
    private val deleteListener: (ProductAndCategory) -> Unit
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
        holder.bindView(products[position])
    }

    override fun getItemCount() = products.size

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position
}