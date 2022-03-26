package io.github.yamin8000.cafe.ui.crud

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentCrudBinding
import io.github.yamin8000.cafe.ui.recyclerview.EmptyAdapter
import io.github.yamin8000.cafe.ui.util.BaseFragment
import io.github.yamin8000.cafe.util.Constants
import io.github.yamin8000.cafe.util.Constants.DATA
import io.github.yamin8000.cafe.util.Constants.IS_EDIT_MODE
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.Alerts.showNullDbError
import io.github.yamin8000.cafe.util.Utility.Alerts.snack
import io.github.yamin8000.cafe.util.Utility.Views.gone
import io.github.yamin8000.cafe.util.Utility.Views.visible
import io.github.yamin8000.cafe.util.Utility.handleCrash
import io.github.yamin8000.cafe.util.Utility.navigate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class CrudFragment<T, VH : RecyclerView.ViewHolder>(
    private val addEditDestination: Int,
) : BaseFragment<FragmentCrudBinding>({ FragmentCrudBinding.inflate(it) }) {

    protected val ioScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.IO) }
    private val mainScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.Main) }

    protected val emptyAdapter by lazy(LazyThreadSafetyMode.NONE) { EmptyAdapter() }

    protected var items = listOf<T>()

    protected var deleteCandidates = mutableListOf<T>()

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
        mainScope.launch { refreshList() }
        binding.crudListDeleteFab.setOnClickListener { deleteFabClickListener() }
        binding.crudListAddButton.setOnClickListener { navigate(addEditDestination) }
    }

    private suspend fun refreshList() {
        items = withContext(ioScope.coroutineContext) { getItems() }
        binding.crudList.layoutManager = LinearLayoutManager(context)
        if (items.isEmpty()) binding.crudList.adapter = emptyAdapter
        else fillList()
    }

    abstract suspend fun getItems(): List<T>

    private fun deleteFabClickListener() {
        navigate(R.id.promptModal)
        setFragmentResultListener(Constants.PROMPT) { _, bundle ->
            if (bundle.getBoolean(Constants.PROMPT_RESULT))
                mainScope.launch { itemsDeleteHandler() }
        }
    }

    private suspend fun itemsDeleteHandler() {
        try {
            dbDeleteAction()
        } catch (e: SQLiteConstraintException) {
            snack(getString(R.string.db_query_restricted), Snackbar.LENGTH_INDEFINITE)
        } catch (e: Exception) {
            snack(getString(R.string.db_update_error), Snackbar.LENGTH_INDEFINITE)
        }
        refreshList()
        binding.crudListDeleteFab.gone()
        deleteCandidates.clear()
    }

    abstract suspend fun dbDeleteAction()

    abstract fun fillList()

    fun deleteCallback(item: T, isChecked: Boolean) {
        binding.crudListDeleteFab.visible()
        if (isChecked) deleteCandidates.add(item)
        else deleteCandidates.remove(item)
        if (deleteCandidates.isEmpty()) binding.crudListDeleteFab.gone()
    }

    fun updateCallback(item: T) {
        navigate(
            addEditDestination,
            bundleOf(
                DATA to item,
                IS_EDIT_MODE to true
            )
        )
    }
}