package io.github.yamin8000.cafe.ui

import android.os.Bundle
import android.view.View
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentNewOrderBinding
import io.github.yamin8000.cafe.db.AppDatabase
import io.github.yamin8000.cafe.db.product.Product
import io.github.yamin8000.cafe.db.product.ProductDao
import io.github.yamin8000.cafe.ui.util.BaseFragment
import io.github.yamin8000.cafe.ui.util.BaseFragmentBundle
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.handleCrash
import io.github.yamin8000.cafe.util.Utility.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewOrderFragment :
    BaseFragment<FragmentNewOrderBinding>({ FragmentNewOrderBinding.inflate(it) }),
    BaseFragmentBundle {

    private val ioScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.IO) }

    private val mainScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.Main) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            if (savedInstanceState == null) handleNew()
            else handleOld(savedInstanceState)
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    override fun handleNew() {
        if (db != null) mainScope.launch { handleOkDb(db) }
        else handleNullDb()
    }

    private fun handleNullDb() {
        toast(getString(R.string.db_null_error))
    }

    private suspend fun handleOkDb(db: AppDatabase?) {
        db?.let {
            val productDao = it.productDao()
            addSomeProducts(productDao).join()
            val products = withContext(ioScope.coroutineContext) { productDao.getAll() }
            binding.textView.text = products.joinToString { product -> product.name }
        }
    }

    private suspend fun addSomeProducts(productDao: ProductDao) = ioScope.launch {
        productDao.insertAll(
            Product("Espresso"),
            Product("Latte"),
            Product("Coffee")
        )
    }

    override fun handleOld(bundle: Bundle?) {
        //TODO("Not yet implemented")
    }
}