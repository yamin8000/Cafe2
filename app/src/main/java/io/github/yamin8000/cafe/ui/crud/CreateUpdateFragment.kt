package io.github.yamin8000.cafe.ui.crud

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.viewbinding.ViewBinding
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.ui.util.BaseFragment
import io.github.yamin8000.cafe.ui.util.Inflater
import io.github.yamin8000.cafe.util.Utility.Alerts.snack
import io.github.yamin8000.cafe.util.Utility.Bundles.data
import io.github.yamin8000.cafe.util.Utility.Bundles.isEditMode
import io.github.yamin8000.cafe.util.Utility.handleCrash
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class CreateUpdateFragment<T : Parcelable, VB : ViewBinding>(
    private val defaultValue: T,
    inflater: Inflater<VB>
) : BaseFragment<VB>(inflater), Crud {

    protected val ioScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.IO) }
    protected val mainScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.Main) }

    private var isEditMode = false
    protected lateinit var item: T

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            isEditMode = arguments.isEditMode()
            item = arguments.data() ?: defaultValue
            init()
            if (isEditMode) initViewForEditMode()
            else initViewForCreateMode()
            confirm()
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    abstract fun init()

    abstract fun initViewForCreateMode()

    abstract fun initViewForEditMode()

    abstract suspend fun createItem()

    abstract suspend fun editItem()

    abstract fun validator(): Boolean

    abstract fun confirm()

    fun confirmListener(validate: () -> Boolean) {
        if (validate()) {
            mainScope.launch {
                if (isEditMode) editItem()
                else createItem()
            }
        } else snack(getString(R.string.enter_all_fields))
    }
}