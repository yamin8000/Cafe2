package io.github.yamin8000.cafe.worker

import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentNewWorkerBinding
import io.github.yamin8000.cafe.db.entities.worker.Worker
import io.github.yamin8000.cafe.ui.crud.CreateUpdateFragment
import io.github.yamin8000.cafe.util.Constants.NO_ID
import io.github.yamin8000.cafe.util.Constants.NO_ID_LONG
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.Alerts.showNullDbError
import io.github.yamin8000.cafe.util.Utility.Alerts.snack
import io.github.yamin8000.cafe.util.Utility.hideKeyboard
import kotlinx.coroutines.withContext

class NewWorkerFragment :
    CreateUpdateFragment<Worker, FragmentNewWorkerBinding>(
        Worker(),
        { FragmentNewWorkerBinding.inflate(it) }
    ) {

    override fun init() {
        //ignore
    }

    override fun initViewForCreateMode() {
        //ignore
    }

    override fun initViewForEditMode() {
        binding.newWorkerConfirm.text = getString(R.string.edit)
        if (item.id != NO_ID_LONG) {
            binding.newWorkerJobEdit.setText(item.job)
            binding.newWorkerNameEdit.setText(item.name)
            binding.newWorkerPhoneEdit.setText(item.phone)
            binding.newWorkerAddressEdit.setText(item.address)
        }
    }

    override suspend fun createItem() {
        val id = withContext(ioScope.coroutineContext) {
            db?.workerDao()?.insert(item)
        }
        if (id != NO_ID.toLong()) workerAddSuccess()
        else showNullDbError()
    }

    private fun workerAddSuccess() {
        snack(getString(R.string.item_add_success, getString(R.string.worker)))
        clear()
    }

    private fun clear() {
        hideKeyboard()
        binding.newWorkerPhoneEdit.text?.clear()
        binding.newWorkerJobEdit.text?.clear()
        binding.newWorkerNameEdit.text?.clear()
        binding.newWorkerAddressEdit.text?.clear()
    }

    override suspend fun editItem() {
        if (item.id != NO_ID_LONG) {
            withContext(ioScope.coroutineContext) {
                db?.workerDao()?.update(item)
            }
            snack(getString(R.string.item_edit_success, getString(R.string.worker)))
            hideKeyboard()
        }
    }

    override fun validator(): Boolean {
        return binding.newWorkerJobEdit.text.toString().isNotBlank() &&
                binding.newWorkerNameEdit.text.toString().isNotBlank() &&
                binding.newWorkerPhoneEdit.text.toString().isNotBlank() &&
                binding.newWorkerAddressEdit.text.toString().isNotBlank()
    }

    override fun confirm() {
        binding.newWorkerConfirm.setOnClickListener {
            item.name = binding.newWorkerNameEdit.text.toString()
            item.phone = binding.newWorkerPhoneEdit.text.toString()
            item.job = binding.newWorkerJobEdit.text.toString()
            item.address = binding.newWorkerAddressEdit.text.toString()
            confirmListener(this::validator)
        }
    }
}