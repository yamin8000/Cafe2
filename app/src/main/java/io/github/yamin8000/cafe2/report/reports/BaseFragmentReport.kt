/*
 *     Cafe/Cafe.app.main
 *     BaseFragmentReport.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     BaseFragmentReport.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe2.report.reports

import android.content.Intent
import androidx.viewbinding.ViewBinding
import io.github.yamin8000.cafe2.ui.recyclerview.EmptyAdapter
import io.github.yamin8000.cafe2.ui.util.BaseFragment
import io.github.yamin8000.cafe2.ui.util.Inflater
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseFragmentReport<T, VB : ViewBinding>(inflater: Inflater<VB>) :
    BaseFragment<VB>(inflater) {

    protected val ioScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.IO) }

    protected val emptyAdapter by lazy(LazyThreadSafetyMode.NONE) { EmptyAdapter() }

    protected var items = listOf<T>()

    protected abstract suspend fun getItems(): List<T>

    protected abstract suspend fun filterItems(items: List<T>): List<T>

    protected abstract fun createList(items: List<T>)

    protected suspend fun handleArguments() {
        if (items.isEmpty())
            items = withContext(ioScope.coroutineContext) { getItems() }
        val filteredItems = withContext(ioScope.coroutineContext) { filterItems(items) }
        createList(filteredItems)
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