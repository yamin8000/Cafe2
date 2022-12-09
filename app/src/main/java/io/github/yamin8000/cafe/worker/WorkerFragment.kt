/*
 *     Cafe/Cafe.app.main
 *     WorkerFragment.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     WorkerFragment.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe.worker

import androidx.recyclerview.widget.GridLayoutManager
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.db.entities.worker.Worker
import io.github.yamin8000.cafe.ui.crud.ReadDeleteFragment
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.Alerts.snack
import kotlinx.coroutines.withContext

class WorkerFragment : ReadDeleteFragment<Worker, WorkerHolder>(R.id.newWorkerFragment) {

    override suspend fun getItems(): List<Worker> {
        return withContext(ioScope.coroutineContext) {
            db.workerDao().getAll()
        }
    }

    override suspend fun dbDeleteAction() {
        withContext(ioScope.coroutineContext) {
            db.workerDao().deleteAll(deleteCandidates)
        }
        snack(getString(R.string.item_delete_success, getString(R.string.workers)))
    }

    override fun fillList() {
        WorkerAdapter(this::updateCallback, this::deleteCallback).apply {
            binding.crudList.adapter = this
            context?.let { binding.crudList.layoutManager = GridLayoutManager(it, 2) }
            this.asyncList.submitList(items)
        }
    }
}