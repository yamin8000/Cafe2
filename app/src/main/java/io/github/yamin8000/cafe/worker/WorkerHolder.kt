/*
 *     Cafe/Cafe.app.main
 *     WorkerHolder.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     WorkerHolder.kt Last modified at 2022/12/9
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

import androidx.recyclerview.widget.AsyncListDiffer
import io.github.yamin8000.cafe.databinding.WorkerItemBinding
import io.github.yamin8000.cafe.db.entities.worker.Worker
import io.github.yamin8000.cafe.ui.crud.CrudHolder

class WorkerHolder(
    asyncList: AsyncListDiffer<Worker>,
    binding: WorkerItemBinding,
    updateCallback: (Worker) -> Unit,
    deleteCallback: (Worker, Boolean) -> Unit
) : CrudHolder<Worker, WorkerItemBinding>(
    asyncList,
    binding,
    updateCallback,
    deleteCallback,
    { worker, _ ->
        binding.workerName.text = worker.name
        binding.workerPhone.text = worker.phone
        binding.workerJob.text = worker.job
        binding.workerAddress.text = worker.address
    }
)