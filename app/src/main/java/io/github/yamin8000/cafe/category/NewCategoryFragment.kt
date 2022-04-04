package io.github.yamin8000.cafe.category

import androidx.fragment.app.setFragmentResultListener
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentNewCategoryBinding
import io.github.yamin8000.cafe.db.entities.category.Category
import io.github.yamin8000.cafe.db.helpers.DbHelpers.newCategory
import io.github.yamin8000.cafe.ui.crud.CreateUpdateFragment
import io.github.yamin8000.cafe.util.Constants.ICON_PICKER
import io.github.yamin8000.cafe.util.Constants.ICON_PICKER_RESULT
import io.github.yamin8000.cafe.util.Constants.NO_ID
import io.github.yamin8000.cafe.util.Constants.NO_ID_LONG
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.Alerts.showNullDbError
import io.github.yamin8000.cafe.util.Utility.Alerts.snack
import io.github.yamin8000.cafe.util.Utility.Views.setImageFromResourceId
import io.github.yamin8000.cafe.util.Utility.hideKeyboard
import io.github.yamin8000.cafe.util.Utility.navigate
import kotlinx.coroutines.withContext

class NewCategoryFragment :
    CreateUpdateFragment<Category, FragmentNewCategoryBinding>(
        Category(),
        { FragmentNewCategoryBinding.inflate(it) }
    ) {

    override fun init() {
        binding.categoryImageButton.setOnClickListener { showIconPicker() }
    }

    override fun initViewForCreateMode() {
        //ignored
    }

    override fun initViewForEditMode() {
        binding.addCategoryConfirm.text = getString(R.string.edit)
        if (item.id != NO_ID_LONG) {
            binding.categoryNameEdit.setText(item.name)
            binding.categoryImage.setImageFromResourceId(item.imageId)
        }
    }

    override suspend fun createItem() {
        val id = ioScope.coroutineContext.newCategory(item)
        if (id != NO_ID.toLong()) categoryAddSuccess()
        else showNullDbError()
    }

    override suspend fun editItem() {
        if (item.id != NO_ID_LONG) {
            withContext(ioScope.coroutineContext) {
                db?.categoryDao()?.update(item)
            }
            snack(getString(R.string.item_edit_success, getString(R.string.category)))
            hideKeyboard()
        }
    }

    override fun validator(): Boolean {
        return item.name.isNotBlank()
    }

    override fun confirm() {
        binding.addCategoryConfirm.setOnClickListener {
            val categoryName = binding.categoryNameEdit.text.toString()
            if (item.imageId == 0)
                item.imageId = R.drawable.pack_top_view_coffee
            item.name = categoryName
            confirmListener(this::validator)
        }
    }

    private fun showIconPicker() {
        navigate(R.id.action_newCategoryFragment_to_iconPickerModal)
        setFragmentResultListener(ICON_PICKER) { _, bundle ->
            item.imageId = bundle.getInt(ICON_PICKER_RESULT)
            binding.categoryImage.setImageFromResourceId(item.imageId)
        }
    }

    private fun categoryAddSuccess() {
        snack(getString(R.string.item_add_success, getString(R.string.category)))
        clear()
    }

    private fun clear() {
        hideKeyboard()
        binding.categoryImage.setImageDrawable(null)
        binding.categoryNameEdit.text?.clear()
        item = Category()
    }
}
