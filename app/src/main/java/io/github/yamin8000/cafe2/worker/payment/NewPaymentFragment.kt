/*
 *     Cafe/Cafe.app.main
 *     NewPaymentFragment.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     NewPaymentFragment.kt Last modified at 2022/12/9
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

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import io.github.yamin8000.cafe2.R
import io.github.yamin8000.cafe2.databinding.FragmentNewPaymentBinding
import io.github.yamin8000.cafe2.db.entities.relatives.PaymentAndWorker
import io.github.yamin8000.cafe2.db.entities.worker.Worker
import io.github.yamin8000.cafe2.db.entities.worker.payment.Payment
import io.github.yamin8000.cafe2.ui.crud.CreateUpdateFragment
import io.github.yamin8000.cafe2.util.Constants.NOT_CREATED_ID
import io.github.yamin8000.cafe2.util.Constants.db
import io.github.yamin8000.cafe2.util.DateTimeUtils
import io.github.yamin8000.cafe2.util.Utility.Alerts.snack
import io.github.yamin8000.cafe2.util.Utility.Views.getNumber
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewPaymentFragment :
    CreateUpdateFragment<PaymentAndWorker, FragmentNewPaymentBinding>(
        PaymentAndWorker(Payment(), Worker()),
        { FragmentNewPaymentBinding.inflate(it) }
    ) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(binding.newPaymentConfirm)
        lifecycleScope.launch { handleWorkersAutoComplete() }
    }

    private suspend fun handleWorkersAutoComplete() {
        val workers = withContext(ioScope.coroutineContext) { db.workerDao().getAll() }
        if (workers.isNotEmpty())
            fillWorkersAutoComplete(workers)
        else handleEmptyWorkers()
    }

    private fun fillWorkersAutoComplete(workers: List<Worker>) {
        binding.paymentWorkerInput.isEnabled = true
        context?.let {
            ArrayAdapter(it, R.layout.dropdown_item, workers).let { adapter ->
                binding.paymentWorkerAuto.setAdapter(adapter)
                binding.paymentWorkerAuto.setOnItemClickListener { parent, _, position, _ ->
                    val worker = parent.getItemAtPosition(position) as Worker
                    item.payment.workerId = worker.id
                }
            }
        }
    }

    private fun handleEmptyWorkers() {
        binding.paymentWorkerInput.isEnabled = false
        binding.newPaymentConfirm.isEnabled = false
        snack(getString(R.string.no_worker_to_pay))
    }

    override fun initViewForCreateMode() {
        //ignore
    }

    override fun initViewForEditMode() {
        binding.paymentValueEdit.setText(item.payment.value.toString())
        item.payment.description?.let { binding.paymentDescriptionEdit.setText(it) }
        lifecycleScope.launch {
            db.workerDao().getById(item.worker.id)?.let { worker ->
                binding.paymentWorkerAuto.setText(worker.name)
            }
        }
    }

    override suspend fun createItem() {
        withContext(ioScope.coroutineContext) { db.paymentDao().insert(item.payment) }
        addSuccess(getString(R.string.payment))
    }

    override suspend fun editItem() {
        if (item.payment.isCreated()) {
            withContext(ioScope.coroutineContext) { db.paymentDao().update(item.payment) }
            editSuccess(getString(R.string.payment))
        }
    }

    override fun validator(): Boolean {
        return binding.paymentValueEdit.text.toString().isNotBlank() &&
                binding.paymentWorkerAuto.text.toString().isNotBlank() &&
                item.payment.workerId != NOT_CREATED_ID
    }

    override fun confirm() {
        binding.newPaymentConfirm.setOnClickListener {
            item.payment.value = binding.paymentValueEdit.getNumber()
            item.payment.date = DateTimeUtils.zonedNow()
            binding.paymentDescriptionEdit.text?.toString().let { description ->
                if (!description.isNullOrBlank())
                    item.payment.description = description
            }
            //item.workerId, already set using auto complete click listener
            confirmItem()
        }
    }

    override fun resetViews() {
        binding.paymentDescriptionEdit.text?.clear()
        binding.paymentValueEdit.text?.clear()
        binding.paymentWorkerAuto.text.clear()
    }
}