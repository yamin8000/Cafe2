package io.github.yamin8000.cafe.worker.payment

import android.widget.ArrayAdapter
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentNewPaymentBinding
import io.github.yamin8000.cafe.db.entities.relatives.PaymentAndWorker
import io.github.yamin8000.cafe.db.entities.worker.Worker
import io.github.yamin8000.cafe.db.entities.worker.payment.Payment
import io.github.yamin8000.cafe.ui.crud.CreateUpdateFragment
import io.github.yamin8000.cafe.util.Constants.NO_ID_LONG
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.DateTimeUtils
import io.github.yamin8000.cafe.util.Utility.Alerts.snack
import io.github.yamin8000.cafe.util.Utility.Views.getNumber
import io.github.yamin8000.cafe.util.Utility.hideKeyboard
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewPaymentFragment :
    CreateUpdateFragment<PaymentAndWorker, FragmentNewPaymentBinding>(
        PaymentAndWorker(Payment(), Worker()),
        { FragmentNewPaymentBinding.inflate(it) }
    ) {

    override fun init() {
        lifecycleScope.launch { handleWorkersAutoComplete() }
    }

    private suspend fun handleWorkersAutoComplete() {
        val workers = withContext(ioScope.coroutineContext) {
            db?.workerDao()?.getAll() ?: listOf()
        }
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
        binding.newPaymentConfirm.text = getString(R.string.edit)
        binding.paymentValueEdit.setText(item.payment.value.toString())
        item.payment.description?.let { binding.paymentDescriptionEdit.setText(it) }
        lifecycleScope.launch {
            db?.workerDao()?.getById(item.worker.id)?.let { worker ->
                binding.paymentWorkerAuto.setText(worker.name)
            }
        }
    }

    override suspend fun createItem() {
        val id = withContext(ioScope.coroutineContext) {
            db?.paymentDao()?.insert(item.payment)
        }
        if (id != null) paymentAddSuccess()
        else snack(getString(R.string.db_update_error))
    }

    private fun paymentAddSuccess() {
        snack(getString(R.string.item_add_success, getString(R.string.payment)))
        clearViews()
        clearValues()
        hideKeyboard()
    }

    private fun clearValues() {
        item = PaymentAndWorker(Payment(), Worker())
    }

    private fun clearViews() {
        binding.paymentDescriptionEdit.text?.clear()
        binding.paymentValueEdit.text?.clear()
        binding.paymentWorkerAuto.text.clear()
    }

    override suspend fun editItem() {
        if (item.payment.id != NO_ID_LONG) {
            val id = withContext(ioScope.coroutineContext) {
                db?.paymentDao()?.update(item.payment)
            }
            if (id != null)
                snack(getString(R.string.item_edit_success, getString(R.string.payment)))
            else snack(getString(R.string.db_update_error))
        }
    }

    override fun validator(): Boolean {
        return binding.paymentValueEdit.text.toString().isNotBlank() &&
                binding.paymentWorkerAuto.text.toString().isNotBlank() &&
                item.payment.workerId != NO_ID_LONG
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
            confirmListener(this::validator)
        }
    }
}