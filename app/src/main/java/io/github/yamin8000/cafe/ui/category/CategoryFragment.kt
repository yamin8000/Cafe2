package io.github.yamin8000.cafe.ui.category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentCategoryBinding
import io.github.yamin8000.cafe.ui.util.BaseFragment
import io.github.yamin8000.cafe.util.Constants.CATEGORY_IMAGE_ID
import io.github.yamin8000.cafe.util.Constants.CATEGORY_NAME
import io.github.yamin8000.cafe.util.Utility.handleCrash
import io.github.yamin8000.cafe.util.Utility.navigate
import io.github.yamin8000.cafe.util.Utility.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class CategoryFragment :
    BaseFragment<FragmentCategoryBinding>({ FragmentCategoryBinding.inflate(it) }) {

    private val ioScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.IO) }
    private val mainScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.Main) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            binding.addCategoryButton.setOnClickListener { addCategoryHandler() }
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    private fun addCategoryHandler() {
        navigate(R.id.action_categoryFragment_to_addCategoryModal)
        setFragmentResultListener(CATEGORY_NAME) { _, bundle ->
            val categoryName = bundle.getString(CATEGORY_NAME) ?: "nothing observed"
            val categoryImageId = bundle.getInt(CATEGORY_IMAGE_ID)
            toast(categoryName)
        }
    }
}