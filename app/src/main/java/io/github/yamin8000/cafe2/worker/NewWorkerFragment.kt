/*
 *     Cafe/Cafe.app.main
 *     NewWorkerFragment.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     NewWorkerFragment.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe2.worker

import android.os.Bundle
import android.view.View
import io.github.yamin8000.cafe2.R
import io.github.yamin8000.cafe2.databinding.FragmentNewWorkerBinding
import io.github.yamin8000.cafe2.db.entities.worker.Worker
import io.github.yamin8000.cafe2.ui.crud.CreateUpdateFragment
import io.github.yamin8000.cafe2.util.Constants.db
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