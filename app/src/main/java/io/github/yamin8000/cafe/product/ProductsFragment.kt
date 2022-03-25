package io.github.yamin8000.cafe.product

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentProductsBinding
import io.github.yamin8000.cafe.db.AppDatabase
import io.github.yamin8000.cafe.db.entities.relatives.ProductAndCategory
import io.github.yamin8000.cafe.ui.util.BaseFragment
import io.github.yamin8000.cafe.util.Constants.PRODUCT
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.Alerts.toast
import io.github.yamin8000.cafe.util.Utility.handleCrash
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

        try {
            if (db != null) mainScope.launch { handleOkDb(db) }
            else handleNullDb()

            mainScope.launch { fillProductsList() }
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    private fun handleOkDb(db: AppDatabase?) {
        db?.let { database ->
            binding.addProductButton.setOnClickListener { addProductClickListener() }
        }
    }

    private suspend fun fillProductsList() {
        val toast = toast(getString(R.string.please_wait))
        val products = getProducts()
        toast.cancel()
        val adapter = ProductsAdapter(this::updateCallback, this::deleteCallback)
//        val adapter = ProductsAdapter(products.toMutableList()) { product ->
//            ioScope.launch { db?.productDao()?.delete(product.product) }
//        }
        binding.productsList.adapter = adapter
        adapter.asyncList.submitList(products)
    }

    private fun updateCallback(productAndCategory: ProductAndCategory) {
        //TODO("Not yet implemented")
    }

    private fun deleteCallback(productAndCategory: ProductAndCategory, isChecked: Boolean) {
        //TODO("Not yet implemented")
    }

    private suspend fun getProducts() = withContext(ioScope.coroutineContext) {
        db?.relativeDao()?.allProductsAndCategories() ?: listOf()
    }

    private fun addProductClickListener() {
        findNavController().navigate(R.id.action_productFragment_to_newProductFragment)
        setFragmentResultListener(PRODUCT) { _, bundle ->

        }
    }

    private fun handleNullDb() {
        toast(getString(R.string.db_null_error))
    }
}