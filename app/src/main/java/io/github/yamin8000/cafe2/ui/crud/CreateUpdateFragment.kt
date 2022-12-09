/*
 *     Cafe/Cafe.app.main
 *     CreateUpdateFragment.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     CreateUpdateFragment.kt Last modified at 2022/12/9
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

import android.database.SQLException
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Button
import androidx.lifecycle.coroutineScope
import androidx.viewbinding.ViewBinding
import io.github.yamin8000.cafe2.R
import io.github.yamin8000.cafe2.ui.util.BaseFragment
import io.github.yamin8000.cafe2.ui.util.Inflater
import io.github.yamin8000.cafe2.util.Utility.Alerts.showDbError
import io.github.yamin8000.cafe2.util.Utility.Alerts.snack
import io.github.yamin8000.cafe2.util.Utility.Bundles.data
import io.github.yamin8000.cafe2.util.Utility.Bundles.isEditMode
import io.github.yamin8000.cafe2.util.Utility.handleCrash
import io.github.yamin8000.cafe2.util.Utility.hideKeyboard
import io.github.yamin8000.cafe2.util.Utility.isSuperuser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class CreateUpdateFragment<T : Parcelable, VB : ViewBinding>(
    private val defaultValue: T,
    inflater: Inflater<VB>
) : BaseFragment<VB>(inflater), Crud {

    protected val ioScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.IO) }
    protected val lifecycleScope by lazy(LazyThreadSafetyMode.NONE) { lifecycle.coroutineScope }

    private var isEditMode = false
    protected var item: T = defaultValue

    private lateinit var confirmButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            isEditMode = arguments.isEditMode()
            item = arguments.data() ?: defaultValue
            if (isEditMode) initViewForEditMode()
            else initViewForCreateMode()
            confirm()
        } catch (e: SQLException) {
            showDbError()
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    abstract fun initViewForCreateMode()

    abstract fun initViewForEditMode()

    abstract suspend fun createItem()

    abstract suspend fun editItem()

    abstract fun validator(): Boolean

    abstract fun confirm()

    abstract fun resetViews()

    fun init(confirmButton: Button) {
        this.confirmButton = confirmButton
        if (isEditMode) confirmButton.text = getString(R.string.edit)
        else confirmButton.text = getString(R.string.confirm)
    }

    fun addSuccess(itemName: String) {
        snack(getString(R.string.item_add_success, itemName))
        hideKeyboard()
        clear()
        confirmButton.isEnabled = true
    }

    fun editSuccess(itemName: String) {
        snack(getString(R.string.item_edit_success, itemName))
        hideKeyboard()
        confirmButton.isEnabled = true
    }

    open fun clear() {
        item = defaultValue
        resetViews()
        binding.root.clearFocus()
    }

    fun confirmItem() {
        if (validator()) {
            lifecycleScope.launch {
                confirmButton.isEnabled = false
                when {
                    !isEditMode -> createItem()
                    isEditMode && isSuperuser() -> editItem()
                    else -> confirmFail(getString(R.string.crud_update_not_allowed))
                }
            }
        } else confirmFail(getString(R.string.enter_all_fields))
    }

    private fun confirmFail(message: String) {
        snack(message)
        confirmButton.isEnabled = true
    }
}