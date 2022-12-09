/*
 *     Cafe/Cafe.app.main
 *     PaymentHolder.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     PaymentHolder.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe2.worker.payment

import androidx.recyclerview.widget.AsyncListDiffer
import io.github.yamin8000.cafe2.R
import io.github.yamin8000.cafe2.databinding.WorkerPaymentItemBinding
import io.github.yamin8000.cafe2.db.entities.relatives.PaymentAndWorker
import io.github.yamin8000.cafe2.ui.crud.CrudHolder
import io.github.yamin8000.cafe2.util.DateTimeUtils.toJalaliIso
import io.github.yamin8000.cafe2.util.Utility.Views.gone
import io.github.yamin8000.cafe2.util.Utility.Views.visible
import io.github.yamin8000.cafe2.util.Utility.numFormat

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
        binding.paymentWorker.text = workerAndPayment.worker.describe()
    }
)