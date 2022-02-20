package io.github.yamin8000.cafe.ui

import android.os.Bundle
import android.view.View
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentProductBinding
import io.github.yamin8000.cafe.db.AppDatabase
import io.github.yamin8000.cafe.db.product.Product
import io.github.yamin8000.cafe.ui.util.BaseFragment
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductFragment :
    BaseFragment<FragmentProductBinding>({ FragmentProductBinding.inflate(it) }) {

    private val ioScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.IO) }
    private val mainScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.Main) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (db != null) mainScope.launch { handleOkDb(db) }
        else handleNullDb()
    }

    private suspend fun handleOkDb(db: AppDatabase?) {
        db?.let { database ->
            refreshProductsTextview(database)
            binding.addProductButton.setOnClickListener { addProductClickListener(database) }
            binding.productsText.setOnClickListener {
                mainScope.launch { refreshProductsTextview(database) }
            }
        }
    }

    private fun addProductClickListener(db: AppDatabase) {
        val productName = binding.productNameEdit.text.toString()
        if (productName.isNotBlank()) {
            val toast = toast(getString(R.string.please_wait))
            ioScope.launch {
                db.productDao().insertAll(Product(productName))
                withContext(mainScope.coroutineContext) { binding.productNameEdit.text?.clear() }
                refreshProductsTextview(db)
            }
            toast.cancel()
        } else toast(getString(R.string.name_cannot_be_empty))
    }

    private suspend fun refreshProductsTextview(db: AppDatabase) {
        val toast = withContext(mainScope.coroutineContext) {
            toast(getString(R.string.please_wait))
        }
        val products = withContext(ioScope.coroutineContext) { db.productDao().getAll() }
        toast.cancel()
        withContext(mainScope.coroutineContext) {
            binding.productsText.text = products.joinToString { product -> product.name }
        }
    }

    private fun handleNullDb() {
        toast(getString(R.string.db_null_error))
    }
}