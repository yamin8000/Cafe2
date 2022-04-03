package io.github.yamin8000.cafe.worker

import io.github.yamin8000.cafe.databinding.WorkerItemBinding
import io.github.yamin8000.cafe.db.entities.worker.Worker
import io.github.yamin8000.cafe.ui.crud.CrudAdapter
import io.github.yamin8000.cafe.ui.recyclerview.AsyncDifferHelper.getAsyncDiffer

class WorkerAdapter(
    private val updateCallback: (Worker) -> Unit,
    private val deleteCallback: (Worker, Boolean) -> Unit
) : CrudAdapter<Worker, WorkerHolder>() {

    override var asyncList = this.getAsyncDiffer<Worker, WorkerHolder>(
        { old, new -> old.id == new.id },
        { old, new -> old == new }
    )

    init {
        initAdapter({ parent, inflater ->
            val binding = WorkerItemBinding.inflate(inflater, parent, false)
            WorkerHolder(asyncList, binding, updateCallback, deleteCallback)
        }, { holder, position ->
            holder.bind(asyncList.currentList[position])
        }, { asyncList.currentList.size })
    }
}