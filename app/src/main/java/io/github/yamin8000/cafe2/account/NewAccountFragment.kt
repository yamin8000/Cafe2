/*
 *     Cafe/Cafe.app.main
 *     NewAccountFragment.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     NewAccountFragment.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe2.account

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.google.android.material.snackbar.Snackbar
import io.github.yamin8000.cafe2.R
import io.github.yamin8000.cafe2.databinding.NewAccountFragmentBinding
import io.github.yamin8000.cafe2.db.entities.account.Account
import io.github.yamin8000.cafe2.db.entities.account.AccountPermission
import io.github.yamin8000.cafe2.ui.crud.CreateUpdateFragment
import io.github.yamin8000.cafe2.util.Constants.db
import io.github.yamin8000.cafe2.util.Utility.Alerts.snack
import io.github.yamin8000.cafe2.util.Utility.Hashes.bCrypt
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewAccountFragment :
    CreateUpdateFragment<Account, NewAccountFragmentBinding>(
        Account(),
        { NewAccountFragmentBinding.inflate(it) }
    ) {

    private lateinit var oldPassword: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(binding.newAccountSubmit)
        oldPassword = item.password
    }

    override fun initViewForCreateMode() {
        lifecycleScope.launch { handleAccountPermission() }
    }

    private suspend fun handleAccountPermission() {
        val accounts = withContext(ioScope.coroutineContext) { db.accountDao().getAll() }
        if (accounts.isNullOrEmpty()) setFirstUserPermission()
        else fillAutocomplete()
    }

    private fun fillAutocomplete() {
        context?.let {
            ArrayAdapter(
                it,
                R.layout.dropdown_item,
                AccountPermission.values()
            ).let { adapter ->
                binding.newAccountPermissionEdit.setAdapter(adapter)
                binding.newAccountPermissionEdit.setOnItemClickListener { parent, _, position, _ ->
                    val permission = parent.adapter.getItem(position) as AccountPermission
                    item.permission = permission
                }
            }
        }
    }

    private fun setFirstUserPermission() {
        binding.newAccountPermissionEdit.apply {
            setText(getString(R.string.superuser))
            isEnabled = false
        }
        item.permission = AccountPermission.Superuser
        snack(getString(R.string.first_account_is_superuser), Snackbar.LENGTH_INDEFINITE)
    }

    override fun initViewForEditMode() {
        fillAutocomplete()
        binding.newAccountSubmit.text = getString(R.string.edit)
        if (item.isCreated()) {
            binding.accountUserLoginEdit.setText(item.username)
            binding.accountPassLoginEdit.setText(item.password)
            binding.newAccountPermissionEdit.setText(item.permission.toString())
        }
    }

    override suspend fun createItem() {
        withContext(ioScope.coroutineContext) { db.accountDao().insert(item) }
        addSuccess(getString(R.string.account))
    }

    override suspend fun editItem() {
        if (item.isCreated()) {
            withContext(ioScope.coroutineContext) { db.accountDao().update(item) }
            editSuccess(getString(R.string.account))
        }
    }

    override fun validator(): Boolean {
        return item.username.isNotBlank() && item.password.isNotBlank()
    }

    override fun confirm() {
        binding.newAccountSubmit.setOnClickListener {
            item.username = binding.accountUserLoginEdit.text.toString()
            item.password = handleNewPassword()
            confirmItem()
        }
    }

    private fun handleNewPassword(): String {
        return if (binding.accountPassLoginEdit.text.toString() == oldPassword) oldPassword
        else binding.accountPassLoginEdit.text.toString().bCrypt()
    }

    override fun resetViews() {
        binding.accountUserLoginEdit.text?.clear()
        binding.accountPassLoginEdit.text?.clear()
        binding.newAccountPermissionEdit.text?.clear()
    }
}