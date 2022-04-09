package io.github.yamin8000.cafe.worker.schedule

import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.db.entities.relatives.ScheduleAndWorker
import io.github.yamin8000.cafe.ui.crud.ReadDeleteFragment

class ScheduleFragment :
    ReadDeleteFragment<ScheduleAndWorker, ScheduleHolder>(R.id.newScheduleFragment) {
    override suspend fun getItems(): List<ScheduleAndWorker> {
        TODO("Not yet implemented")
    }

    override suspend fun dbDeleteAction() {
        TODO("Not yet implemented")
    }

    override fun fillList() {
        TODO("Not yet implemented")
    }

}