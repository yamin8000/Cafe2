package io.github.yamin8000.cafe.ui.crud

import android.database.SQLException
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Button
import androidx.lifecycle.coroutineScope
import androidx.viewbinding.ViewBinding
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.ui.util.BaseFragment
import io.github.yamin8000.cafe.ui.util.Inflater
import io.github.yamin8000.cafe.util.Utility.Alerts.showDbError
import io.github.yamin8000.cafe.util.Utility.Alerts.snack
import io.github.yamin8000.cafe.util.Utility.Bundles.data
import io.github.yamin8000.cafe.util.Utility.Bundles.isEditMode
import io.github.yamin8000.cafe.util.Utility.handleCrash
import io.github.yamin8000.cafe.util.Utility.hideKeyboard
import io.github.yamin8000.cafe.util.Utility.isSuperuser
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