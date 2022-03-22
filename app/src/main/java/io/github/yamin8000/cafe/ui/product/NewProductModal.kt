package io.github.yamin8000.cafe.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.NewProductModalBinding
import io.github.yamin8000.cafe.db.entities.product.Product
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.handleCrash
import io.github.yamin8000.cafe.util.Utility.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewProductModal : BottomSheetDialogFragment() {

    private val ioScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.IO) }
    private val mainScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.Main) }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        NewProductModalBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            binding.addProductConfirm.setOnClickListener {
                val productName = binding.productNameEdit.text.toString()
                if (productName.isNotBlank()) {
                    val toast = toast(getString(R.string.please_wait))
                    ioScope.launch {
                        db?.productDao()?.insertAll(Product(productName, 0))
                        withContext(mainScope.coroutineContext) {
                            binding.productNameEdit.text?.clear()
                            //fillProductsList()
                        }
                    }
                    toast.cancel()
                } else toast(getString(R.string.name_cannot_be_empty))
            }
        } catch (e: Exception) {
            handleCrash(e)
        }
    }
}