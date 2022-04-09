package io.github.yamin8000.cafe.worker.schedule

import androidx.recyclerview.widget.AsyncListDiffer
import io.github.yamin8000.cafe.databinding.ScheduleItemBinding
import io.github.yamin8000.cafe.db.entities.relatives.ScheduleAndWorker
import io.github.yamin8000.cafe.ui.crud.CrudHolder
import io.github.yamin8000.cafe.util.Utility.Views.handleData

class ScheduleHolder(
    asyncList: AsyncListDiffer<ScheduleAndWorker>,
    binding: ScheduleItemBinding,
    updateCallback: (ScheduleAndWorker) -> Unit,
    deleteCallback: (ScheduleAndWorker, Boolean) -> Unit
) : CrudHolder<ScheduleAndWorker, ScheduleItemBinding>(
    asyncList,
    binding,
    updateCallback,
    deleteCallback,
    { scheduleAndWorker, _ ->
        binding.scheduleDay.text = scheduleAndWorker.schedule.day.toString()
        binding.scheduleDescription.handleData(scheduleAndWorker.schedule.description)
        binding.scheduleEnd.handleData(scheduleAndWorker.schedule.end.toString())
        binding.scheduleStart.handleData(scheduleAndWorker.schedule.start.toString())
        binding.scheduleWorker.handleData(scheduleAndWorker.worker.describe())
    }
)