package io.github.yamin8000.cafe.worker.payment

import io.github.yamin8000.cafe.databinding.WorkerPaymentItemBinding
import io.github.yamin8000.cafe.db.entities.relatives.PaymentAndWorker
import io.github.yamin8000.cafe.ui.crud.CrudAdapter
import io.github.yamin8000.cafe.ui.recyclerview.AsyncDifferHelper.getAsyncDiffer

class PaymentAdapter(
    private val updateCallback: (PaymentAndWorker) -> Unit,
    private val deleteCallback: (PaymentAndWorker, Boolean) -> Unit
) : CrudAdapter<PaymentAndWorker, PaymentHolder>() {

    override var asyncList = this.getAsyncDiffer<PaymentAndWorker, PaymentHolder>(
        { old, new -> old.payment.id == new.payment.id },
        { old, new -> old == new }
    )

    init {
        initAdapter({ parent, inflater ->
            val binding = WorkerPaymentItemBinding.inflate(inflater, parent, false)
            PaymentHolder(asyncList, binding, updateCallback, deleteCallback)
        }, { holder, position ->
            holder.bind(asyncList.currentList[position])
        }, { asyncList.currentList.size })
    }
}