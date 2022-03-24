package io.github.yamin8000.cafe.category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentCategoryBinding
import io.github.yamin8000.cafe.db.entities.category.Category
import io.github.yamin8000.cafe.db.helpers.DbHelpers.deleteCategories
import io.github.yamin8000.cafe.db.helpers.DbHelpers.fetchCategories
import io.github.yamin8000.cafe.db.helpers.DbHelpers.newCategory
import io.github.yamin8000.cafe.ui.recyclerview.adapters.EmptyAdapter
import io.github.yamin8000.cafe.ui.util.BaseFragment
import io.github.yamin8000.cafe.util.Constants.CATEGORY_IMAGE_ID
import io.github.yamin8000.cafe.util.Constants.CATEGORY_NAME
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.gone
import io.github.yamin8000.cafe.util.Utility.handleCrash
import io.github.yamin8000.cafe.util.Utility.navigate
import io.github.yamin8000.cafe.util.Utility.showNullDbError
import io.github.yamin8000.cafe.util.Utility.toast
import io.github.yamin8000.cafe.util.Utility.visible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryFragment :
    BaseFragment<FragmentCategoryBinding>({ FragmentCategoryBinding.inflate(it) }) {

    private val ioScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.IO) }
    private val mainScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.Main) }

    private var categories = listOf<Category>()

    private val listAdapter by lazy(LazyThreadSafetyMode.NONE) { CategoryAdapter(this::deleteClickListener) }

    private val emptyAdapter by lazy(LazyThreadSafetyMode.NONE) { EmptyAdapter() }

    private var deleteCandidates = mutableListOf<Category>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            if (db != null) prepareUi()
            else showNullDbError()
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    private fun prepareUi() {
        mainScope.launch { refreshCategoriesList() }
        binding.addCategoryButton.setOnClickListener { addCategoryHandler() }
        binding.deleteFab.setOnClickListener { mainScope.launch { categoriesDeleterHandler() } }
    }

    private suspend fun refreshCategoriesList() {
        categories = ioScope.coroutineContext.fetchCategories()
        if (categories.isEmpty()) binding.categoriesList.adapter = emptyAdapter
        else populateList()
    }

    private suspend fun categoriesDeleterHandler() {
        ioScope.coroutineContext.deleteCategories(*deleteCandidates.toTypedArray())
        refreshCategoriesList()
        binding.deleteFab.gone()
        deleteCandidates.clear()
    }

    private fun populateList() {
        binding.categoriesList.adapter = listAdapter
        listAdapter.asyncList.submitList(categories)
    }

    private fun deleteClickListener(category: Category, isChecked: Boolean) {
        binding.deleteFab.visible()
        if (isChecked) deleteCandidates.add(category)
        else deleteCandidates.remove(category)
        if (deleteCandidates.isEmpty()) binding.deleteFab.gone()
    }

    private fun addCategoryHandler() {
        navigate(R.id.action_categoryFragment_to_addCategoryModal)
        setFragmentResultListener(CATEGORY_NAME) { _, bundle ->
            val categoryName = bundle.getString(CATEGORY_NAME) ?: ""
            var categoryImageId = bundle.getInt(CATEGORY_IMAGE_ID)
            if (categoryImageId == 0) categoryImageId = R.drawable.pack_top_view_coffee
            if (categoryName.isNotBlank()) {
                mainScope.launch { addNewCategory(Category(categoryName, categoryImageId)) }
            } else toast(getString(R.string.category_cannot_be_empty))
        }
    }

    private suspend fun addNewCategory(category: Category) {
        val id = ioScope.coroutineContext.newCategory(category)
        if (id != -1L) refreshCategoriesList()
        else showNullDbError()
    }
}