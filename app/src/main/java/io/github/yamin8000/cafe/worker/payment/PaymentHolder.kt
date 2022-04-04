package io.github.yamin8000.cafe.worker.payment

import androidx.recyclerview.widget.AsyncListDiffer
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.WorkerPaymentItemBinding
import io.github.yamin8000.cafe.db.entities.relatives.PaymentAndWorker
import io.github.yamin8000.cafe.ui.crud.CrudHolder
import io.github.yamin8000.cafe.util.DateTimeUtils.toJalaliIso
import io.github.yamin8000.cafe.util.Utility.Views.gone
import io.github.yamin8000.cafe.util.Utility.Views.visible
import io.github.yamin8000.cafe.util.Utility.numFormat

class PaymentHolder(
    asyncList: AsyncListDiffer<PaymentAndWorker>,
    binding: WorkerPaymentItemBinding,
    updateCallback: (PaymentAndWorker) -> Unit,
    deleteCallback: (PaymentAndWorker, Boolean) -> Unit
) : CrudHolder<PaymentAndWorker, WorkerPaymentItemBinding>(
    asyncList,
    binding,
    updateCallback,
    deleteCallback,
    { workerAndPayment, context ->
        val paymentValue = workerAndPayment.payment.value.toString().numFormat()
        binding.paymentValue.text = context.getString(R.string.rial_template, paymentValue)
        binding.paymentDate.text = workerAndPayment.payment.date.toJalaliIso()
        workerAndPayment.payment.description.let {
            binding.paymentDescription.apply {
                if (!it.isNullOrBlank()) {
                    text = it
                    visible()
                } else gone()
            }
        }
        binding.paymentWorker.text = buildString {
            append(workerAndPayment.worker.name)
            append("\n")
            append(workerAndPayment.worker.job)
            append("\n")
            append(workerAndPayment.worker.phone)
        }
    }
)