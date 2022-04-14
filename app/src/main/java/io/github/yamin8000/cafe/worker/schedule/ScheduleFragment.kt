package io.github.yamin8000.cafe.worker.schedule

import androidx.recyclerview.widget.LinearLayoutManager
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.db.entities.relatives.ScheduleAndWorker
import io.github.yamin8000.cafe.ui.crud.ReadDeleteFragment
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.Alerts.snack
import kotlinx.coroutines.withContext

class ScheduleFragment :
    ReadDeleteFragment<ScheduleAndWorker, ScheduleHolder>(R.id.newScheduleFragment) {

    override suspend fun getItems(): List<ScheduleAndWorker> {
        return withContext(ioScope.coroutineContext) {
            db.relativeDao().getSchedulesAndWorkers()
        }
    }

    override suspend fun dbDeleteAction() {
        withContext(ioScope.coroutineContext) {
            db.scheduleDao().deleteAll(items.map { it.schedule })
        }
        snack(getString(R.string.item_delete_success, getString(R.string.schedule)))
    }

    override fun fillList() {
        ScheduleAdapter(this::updateCallback, this::deleteCallback).apply {
            binding.crudList.adapter = this
            context?.let { binding.crudList.layoutManager = LinearLayoutManager(it) }
            this.asyncList.submitList(items)
        }
    }
}