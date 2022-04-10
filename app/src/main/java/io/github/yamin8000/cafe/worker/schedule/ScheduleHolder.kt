package io.github.yamin8000.cafe.worker.schedule

import androidx.recyclerview.widget.AsyncListDiffer
import io.github.yamin8000.cafe.databinding.ScheduleItemBinding
import io.github.yamin8000.cafe.db.entities.relatives.ScheduleAndWorker
import io.github.yamin8000.cafe.ui.crud.CrudHolder
import io.github.yamin8000.cafe.util.DateTimeUtils.toJalali
import io.github.yamin8000.cafe.util.DateTimeUtils.toJalaliIso
import io.github.yamin8000.cafe.util.Utility.Views.gone
import io.github.yamin8000.cafe.util.Utility.Views.handleData
import io.github.yamin8000.cafe.util.Utility.Views.visible

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
        binding.scheduleDay.text = scheduleAndWorker.schedule.day.toJalali()
        binding.scheduleEnd.handleData(scheduleAndWorker.schedule.end?.toJalaliIso())
        binding.scheduleStart.handleData(scheduleAndWorker.schedule.start?.toJalaliIso())
        binding.scheduleWorker.handleData(scheduleAndWorker.worker.describe())
        scheduleAndWorker.schedule.description.apply {
            if (this != null) {
                binding.scheduleDescription.handleData(this)
                binding.scheduleDescription.visible()
                binding.scheduleDescriptionHolder.visible()
            } else {
                binding.scheduleDescription.gone()
                binding.scheduleDescriptionHolder.gone()
            }
        }
        binding.scheduleDescription.handleData(scheduleAndWorker.schedule.description)
    }
)