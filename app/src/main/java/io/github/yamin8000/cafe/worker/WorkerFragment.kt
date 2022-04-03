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
            db?.workerDao()?.getAll()
        } ?: listOf()
    }

    override suspend fun dbDeleteAction() {
        val id = withContext(ioScope.coroutineContext) {
            db?.workerDao()?.deleteAll(deleteCandidates)
        }
        if (id != null) snack(getString(R.string.item_delete_success, getString(R.string.workers)))
        else snack(getString(R.string.db_update_error))
    }

    override fun fillList() {
        WorkerAdapter(this::updateCallback, this::deleteCallback).apply {
            binding.crudList.adapter = this
            context?.let { binding.crudList.layoutManager = GridLayoutManager(it, 2) }
            this.asyncList.submitList(items)
        }
    }

}