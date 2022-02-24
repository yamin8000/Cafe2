package io.github.yamin8000.cafe.ui.product

import android.os.Bundle
import android.view.View
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentProductsBinding
import io.github.yamin8000.cafe.db.AppDatabase
import io.github.yamin8000.cafe.db.helpers.DbHelpers.fetchProducts
import io.github.yamin8000.cafe.db.product.Product
import io.github.yamin8000.cafe.ui.util.BaseFragment
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductsFragment :
    BaseFragment<FragmentProductsBinding>({ FragmentProductsBinding.inflate(it) }) {

    private val ioScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.IO) }
    private val mainScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.Main) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (db != null) mainScope.launch { handleOkDb(db) }
        else handleNullDb()

        mainScope.launch { fillProductsList() }
    }

    private fun handleOkDb(db: AppDatabase?) {
        db?.let { database ->
            binding.addProductButton.setOnClickListener { addProductClickListener(database) }
        }
    }

    private suspend fun fillProductsList() {
        val toast = toast(getString(R.string.please_wait))
        val products = ioScope.coroutineContext.fetchProducts().toMutableList()
        toast.cancel()
        val adapter = ProductsAdapter(products) { product ->
            ioScope.launch { db?.productDao()?.delete(product) }
        }
        binding.productsList.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun addProductClickListener(db: AppDatabase) {
        val productName = binding.productNameEdit.text.toString()
        if (productName.isNotBlank()) {
            val toast = toast(getString(R.string.please_wait))
            ioScope.launch {
                db.productDao().insertAll(Product(productName))
                withContext(mainScope.coroutineContext) {
                    binding.productNameEdit.text?.clear()
                    fillProductsList()
                }
            }
            toast.cancel()
        } else toast(getString(R.string.name_cannot_be_empty))
    }

    private fun handleNullDb() {
        toast(getString(R.string.db_null_error))
    }
}