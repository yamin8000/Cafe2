package io.github.yamin8000.cafe.account

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.google.android.material.snackbar.Snackbar
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.NewAccountFragmentBinding
import io.github.yamin8000.cafe.db.entities.account.Account
import io.github.yamin8000.cafe.db.entities.account.AccountPermission
import io.github.yamin8000.cafe.ui.crud.CreateUpdateFragment
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.Alerts.snack
import io.github.yamin8000.cafe.util.Utility.Hashes.bCrypt
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