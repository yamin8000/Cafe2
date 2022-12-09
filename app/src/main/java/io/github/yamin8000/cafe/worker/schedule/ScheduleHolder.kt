/*
 *     Cafe/Cafe.app.main
 *     ScheduleHolder.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     ScheduleHolder.kt Last modified at 2022/12/9
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