package io.github.yamin8000.cafe.worker.schedule

import io.github.yamin8000.cafe.databinding.FragmentNewScheduleBinding
import io.github.yamin8000.cafe.db.entities.relatives.ScheduleAndWorker
import io.github.yamin8000.cafe.db.entities.worker.Worker
import io.github.yamin8000.cafe.db.entities.worker.schedule.Schedule
import io.github.yamin8000.cafe.ui.crud.CreateUpdateFragment

class NewScheduleFragment :
    CreateUpdateFragment<ScheduleAndWorker, FragmentNewScheduleBinding>(
        ScheduleAndWorker(Schedule(), Worker()),
        { FragmentNewScheduleBinding.inflate(it) }
    ) {

    override fun init() {
        TODO("Not yet implemented")
    }

    override fun initViewForCreateMode() {
        TODO("Not yet implemented")
    }

    override fun initViewForEditMode() {
        TODO("Not yet implemented")
    }

    override suspend fun createItem() {
        TODO("Not yet implemented")
    }

    override suspend fun editItem() {
        TODO("Not yet implemented")
    }

    override fun validator(): Boolean {
        TODO("Not yet implemented")
    }

    override fun confirm() {
        TODO("Not yet implemented")
    }
}