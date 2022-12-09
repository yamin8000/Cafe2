/*
 *     Cafe/Cafe.app.main
 *     ReadDeleteFragment.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     ReadDeleteFragment.kt Last modified at 2022/12/9
 *     This file is part of Cafe/Cafe.app.main.
 *     Copyright (C) 2022  Yamin Siahmargooei
 *
 *     Cafe/Cafe.app.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cafe/Cafe.app.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cafe.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.cafe2.ui.crud

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.github.yamin8000.cafe2.R
import io.github.yamin8000.cafe2.databinding.FragmentCrudBinding
import io.github.yamin8000.cafe2.ui.recyclerview.EmptyAdapter
import io.github.yamin8000.cafe2.ui.util.BaseFragment
import io.github.yamin8000.cafe2.util.Constants
import io.github.yamin8000.cafe2.util.Constants.CRUD_NAME
import io.github.yamin8000.cafe2.util.Constants.DATA
import io.github.yamin8000.cafe2.util.Constants.IS_EDIT_MODE
import io.github.yamin8000.cafe2.util.Utility.Alerts.snack
import io.github.yamin8000.cafe2.util.Utility.Views.gone
import io.github.yamin8000.cafe2.util.Utility.Views.visible
import io.github.yamin8000.cafe2.util.Utility.handleCrash
import io.github.yamin8000.cafe2.util.Utility.isSuperuser
import io.github.yamin8000.cafe2.util.Utility.navigate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class ReadDeleteFragment<T, VH : RecyclerView.ViewHolder>(
    private val addEditDestination: Int,
) : BaseFragment<FragmentCrudBinding>({ FragmentCrudBinding.inflate(it) }), Crud {

    protected val ioScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.IO) }
    private val lifecycleScope by lazy(LazyThreadSafetyMode.NONE) { lifecycle.coroutineScope }

    protected val emptyAdapter by lazy(LazyThreadSafetyMode.NONE) { EmptyAdapter() }

    protected var items = listOf<T>()

    protected var deleteCandidates = mutableListOf<T>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            prepareUi()
            arguments?.getString(CRUD_NAME)?.let {
                binding.crudName.text = it
            }
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    private fun prepareUi() {
        lifecycleScope.launch { refreshList() }
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
            val isUserDeleting = bundle.getBoolean(Constants.PROMPT_RESULT)
            val isDeleteAllowed = isSuperuser()
            when {
                !isDeleteAllowed -> snack(getString(R.string.crud_delete_not_allowed))
                isUserDeleting && isDeleteAllowed -> lifecycleScope.launch { itemsDeleteHandler() }
            }
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