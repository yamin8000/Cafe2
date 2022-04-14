package io.github.yamin8000.cafe.worker

import android.os.Bundle
import android.view.View
import androidx.core.view.children
import com.google.android.material.textfield.TextInputEditText
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentNewWorkerBinding
import io.github.yamin8000.cafe.db.entities.worker.Worker
import io.github.yamin8000.cafe.ui.crud.CreateUpdateFragment
import io.github.yamin8000.cafe.util.Constants.db
import kotlinx.coroutines.withContext

class NewWorkerFragment :
    CreateUpdateFragment<Worker, FragmentNewWorkerBinding>(
        Worker(),
        { FragmentNewWorkerBinding.inflate(it) }
    ) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(binding.newWorkerConfirm)
    }

    override fun initViewForCreateMode() {
        //ignore
    }

    override fun initViewForEditMode() {
        if (item.isCreated()) {
            binding.newWorkerJobEdit.setText(item.job)
            binding.newWorkerNameEdit.setText(item.name)
            binding.newWorkerPhoneEdit.setText(item.phone)
            binding.newWorkerAddressEdit.setText(item.address)
        }
    }

    override suspend fun createItem() {
        withContext(ioScope.coroutineContext) { db.workerDao().insert(item) }
        addSuccess(getString(R.string.worker))
    }

    override suspend fun editItem() {
        if (item.isCreated()) {
            withContext(ioScope.coroutineContext) { db.workerDao().update(item) }
            editSuccess(getString(R.string.worker))
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
            confirmItem()
        }
    }

    override fun resetViews() {
        binding.newWorkerPhoneEdit.text?.clear()
        binding.newWorkerJobEdit.text?.clear()
        binding.newWorkerNameEdit.text?.clear()
        binding.newWorkerAddressEdit.text?.clear()
    }
}