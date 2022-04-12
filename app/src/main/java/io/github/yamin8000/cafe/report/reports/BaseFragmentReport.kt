package io.github.yamin8000.cafe.report.reports

import android.content.Intent
import android.os.Bundle
import androidx.viewbinding.ViewBinding
import io.github.yamin8000.cafe.ui.recyclerview.EmptyAdapter
import io.github.yamin8000.cafe.ui.util.BaseFragment
import io.github.yamin8000.cafe.ui.util.Inflater
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseFragmentReport<T, VB : ViewBinding>(inflater: Inflater<VB>) :
    BaseFragment<VB>(inflater) {

    protected val ioScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.IO) }

    protected val emptyAdapter by lazy(LazyThreadSafetyMode.NONE) { EmptyAdapter() }

    protected var items = listOf<T>()

    protected abstract suspend fun getItems(): List<T>

    protected abstract suspend fun filterItems(
        items: List<T>,
        arguments: Bundle?
    ): List<T>

    protected abstract fun createList(items: List<T>)

    protected suspend fun handleArguments() {
        items = withContext(ioScope.coroutineContext) { getItems() }
        items = withContext(ioScope.coroutineContext) { filterItems(items, arguments) }
        createList(items)
    }

    protected fun shareCsv(csv: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, csv)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, null))
    }
}