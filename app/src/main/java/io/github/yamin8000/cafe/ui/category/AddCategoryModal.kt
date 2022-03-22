package io.github.yamin8000.cafe.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.yamin8000.cafe.databinding.AddCategoryModalBinding
import io.github.yamin8000.cafe.util.Constants.CATEGORY
import io.github.yamin8000.cafe.util.Utility.handleCrash

class AddCategoryModal : BottomSheetDialogFragment() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        AddCategoryModalBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            binding.button.setOnClickListener {
                val category = binding.categoryNameEdit.text.toString()
                setFragmentResult(CATEGORY, bundleOf(CATEGORY to category))
                dismiss()
            }
        } catch (e: Exception) {
            handleCrash(e)
        }
    }
}