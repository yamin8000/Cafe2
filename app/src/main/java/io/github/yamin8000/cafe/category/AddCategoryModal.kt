package io.github.yamin8000.cafe.category

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.AddCategoryModalBinding
import io.github.yamin8000.cafe.drawable.DrawableAdapter
import io.github.yamin8000.cafe.drawable.Utility.getIconPack
import io.github.yamin8000.cafe.util.Constants.CATEGORY_IMAGE_ID
import io.github.yamin8000.cafe.util.Constants.CATEGORY_NAME
import io.github.yamin8000.cafe.util.Utility.handleCrash
import io.github.yamin8000.cafe.util.Utility.toast

class AddCategoryModal : BottomSheetDialogFragment() {

    private var addImageToggle = true

    private val icons by lazy(LazyThreadSafetyMode.NONE) { getIconPack() }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        AddCategoryModalBinding.inflate(layoutInflater)
    }

    private var categoryName: String? = null
    private var categoryImageId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            binding.categoryImageButton.setOnClickListener { handleDrawablesListToggle() }
            binding.addCategoryConfirm.setOnClickListener { handleCategoryDataConfirmation() }
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    private fun handleCategoryDataConfirmation() {
        categoryName = binding.categoryNameEdit.text.toString()
        if (categoryName.isNullOrBlank()) {
            toast(getString(R.string.category_cannot_be_empty))
        } else confirmCategoryData()
    }

    private fun confirmCategoryData() {
        setFragmentResult(
            CATEGORY_NAME,
            bundleOf(CATEGORY_NAME to categoryName, CATEGORY_IMAGE_ID to categoryImageId)
        )
        dismiss()
    }

    private fun handleDrawablesListToggle() {
        if (addImageToggle) {
            binding.drawablesList.visibility = View.VISIBLE
            if (binding.drawablesList.adapter == null) createList()
            addImageToggle = false
        } else {
            binding.drawablesList.visibility = View.GONE
            addImageToggle = true
        }

    }

    private fun createList() {
        binding.drawablesList.adapter = DrawableAdapter(icons, this::onDrawableClicked)
    }

    private fun onDrawableClicked(id: Int, drawable: Drawable) {
        categoryImageId = id
        binding.categoryImage.setImageDrawable(drawable)
        addImageToggle = !addImageToggle
        binding.drawablesList.visibility = View.GONE
    }
}