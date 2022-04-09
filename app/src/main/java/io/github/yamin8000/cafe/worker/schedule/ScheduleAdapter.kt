package io.github.yamin8000.cafe.worker.schedule

import io.github.yamin8000.cafe.databinding.ScheduleItemBinding
import io.github.yamin8000.cafe.db.entities.relatives.ScheduleAndWorker
import io.github.yamin8000.cafe.ui.crud.CrudAdapter
import io.github.yamin8000.cafe.ui.recyclerview.AsyncDifferHelper.getAsyncDiffer

class ScheduleAdapter(
    private val updateCallback: (ScheduleAndWorker) -> Unit,
    private val deleteCallback: (ScheduleAndWorker, Boolean) -> Unit
) : CrudAdapter<ScheduleAndWorker, ScheduleHolder>() {

    override var asyncList = this.getAsyncDiffer<ScheduleAndWorker, ScheduleHolder>(
        { old, new -> old.schedule.id == new.schedule.id },
        { old, new -> old == new }
    )

    init {
        initAdapter({ parent, inflater ->
            val binding = ScheduleItemBinding.inflate(inflater, parent, false)
            ScheduleHolder(asyncList, binding, updateCallback, deleteCallback)
        }, { holder, position ->
            holder.bind(asyncList.currentList[position])
        }, { asyncList.currentList.size })
    }
}