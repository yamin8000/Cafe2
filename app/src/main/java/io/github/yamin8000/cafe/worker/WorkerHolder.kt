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