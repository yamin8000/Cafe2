package io.github.yamin8000.cafe.category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentNewCategoryBinding
import io.github.yamin8000.cafe.db.entities.category.Category
import io.github.yamin8000.cafe.db.helpers.DbHelpers.newCategory
import io.github.yamin8000.cafe.ui.util.BaseFragment
import io.github.yamin8000.cafe.util.Constants.ICON_PICKER
import io.github.yamin8000.cafe.util.Constants.ICON_PICKER_RESULT
import io.github.yamin8000.cafe.util.Constants.NO_ID
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.Alerts.showNullDbError
import io.github.yamin8000.cafe.util.Utility.Alerts.snack
import io.github.yamin8000.cafe.util.Utility.Bundles.data
import io.github.yamin8000.cafe.util.Utility.Bundles.isEditMode
import io.github.yamin8000.cafe.util.Utility.Views.setImageFromResourceId
import io.github.yamin8000.cafe.util.Utility.handleCrash
import io.github.yamin8000.cafe.util.Utility.hideKeyboard
import io.github.yamin8000.cafe.util.Utility.navigate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewCategoryFragment :
    BaseFragment<FragmentNewCategoryBinding>({ FragmentNewCategoryBinding.inflate(it) }) {

    private val ioScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.IO) }
    private val mainScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.Main) }

    private var categoryImageId = NO_ID

    private var isEditMode = false

    private var editedCategory: Category? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            isEditMode = arguments.isEditMode()
            editedCategory = arguments.data()
            if (isEditMode) initEditMode(editedCategory)
            binding.categoryImageButton.setOnClickListener { showIconPicker() }
            binding.addCategoryConfirm.setOnClickListener { handleCategoryDataConfirmation() }
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    private fun initEditMode(category: Category?) {
        binding.addCategoryConfirm.text = getString(R.string.edit)
        if (category != null) {
            binding.categoryNameEdit.setText(category.name)
            binding.categoryImage.setImageFromResourceId(category.imageId)
        }
    }

    private fun handleCategoryDataConfirmation() {
        val categoryName = binding.categoryNameEdit.text.toString()
        if (categoryName.isNotBlank()) confirmCategoryData(categoryName)
        else snack(getString(R.string.category_cannot_be_empty))
    }

    private fun confirmCategoryData(categoryName: String) {
        if (categoryImageId == NO_ID) categoryImageId = R.drawable.pack_top_view_coffee
        if (categoryName.isNotBlank()) {
            mainScope.launch {
                if (isEditMode) editCategory(editedCategory)
                else addNewCategory(Category(categoryName, categoryImageId))
            }
        } else snack(getString(R.string.category_cannot_be_empty))
    }

    private fun showIconPicker() {
        navigate(R.id.action_newCategoryFragment_to_iconPickerModal)
        setFragmentResultListener(ICON_PICKER) { _, bundle ->
            categoryImageId = bundle.getInt(ICON_PICKER_RESULT)
            binding.categoryImage.setImageFromResourceId(categoryImageId)
        }
    }

    private suspend fun addNewCategory(category: Category) {
        val id = ioScope.coroutineContext.newCategory(category)
        if (id != NO_ID.toLong()) categoryAddSuccess()
        else showNullDbError()
    }

    private fun categoryAddSuccess() {
        snack(getString(R.string.category_add_success))
        clear()
    }

    private fun clear() {
        hideKeyboard()
        binding.categoryImage.setImageDrawable(null)
        binding.categoryNameEdit.text?.clear()
        categoryImageId = NO_ID
    }

    private suspend fun editCategory(category: Category?) {
        if (category != null) {
            category.name = binding.categoryNameEdit.text.toString()
            category.imageId = categoryImageId
            withContext(ioScope.coroutineContext) {
                db?.categoryDao()?.update(category)
            }
            snack(getString(R.string.category_edit_success))
            hideKeyboard()
        }
    }
}
