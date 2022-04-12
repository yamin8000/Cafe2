package io.github.yamin8000.cafe.report.modal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentNewProductBinding
import io.github.yamin8000.cafe.util.Constants.CATEGORY
import io.github.yamin8000.cafe.util.Constants.FILTER
import io.github.yamin8000.cafe.util.Constants.NAME
import io.github.yamin8000.cafe.util.Constants.PRICE
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.Views.autoCompleteAdapter
import io.github.yamin8000.cafe.util.Utility.Views.gone
import io.github.yamin8000.cafe.util.Utility.handleCrash
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductFilterModal : BottomSheetDialogFragment() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentNewProductBinding.inflate(layoutInflater)
    }

    private val ioScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.IO) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            prepareViewsForFilter()
            binding.addProductConfirm.setOnClickListener { sendArguments() }
            lifecycleScope.launch { handleCategoriesAutoComplete() }
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    private suspend fun handleCategoriesAutoComplete() {
        val categories = withContext(ioScope.coroutineContext) {
            db?.categoryDao()?.getAll()
        } ?: listOf()
        context?.let { binding.productCategoryEdit.setAdapter(it.autoCompleteAdapter(categories)) }
    }

    private fun sendArguments() {
        val name = binding.productNameEdit.text.toString()
        val category = binding.productCategoryEdit.text.toString()
        val price = binding.productPriceEdit.text.toString()
        val bundle = Bundle()
        if (name.isNotBlank()) bundle.putString(NAME, name)
        if (category.isNotBlank()) bundle.putString(CATEGORY, category)
        if (price.isNotBlank()) bundle.putString(PRICE, price)
        setFragmentResult(FILTER, bundle)
        dismiss()
    }

    private fun prepareViewsForFilter() {
        binding.productImageButton.gone()
        binding.productImage.gone()
        binding.productPriceInput.hint = getString(R.string.max_product_price)
    }
}