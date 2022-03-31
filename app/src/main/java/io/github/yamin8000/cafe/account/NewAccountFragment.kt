package io.github.yamin8000.cafe.account

import android.widget.ArrayAdapter
import com.google.android.material.snackbar.Snackbar
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.NewAccountFragmentBinding
import io.github.yamin8000.cafe.db.entities.account.Account
import io.github.yamin8000.cafe.db.entities.account.AccountPermission
import io.github.yamin8000.cafe.ui.crud.CreateUpdateFragment
import io.github.yamin8000.cafe.util.Constants.NO_ID_LONG
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.Alerts.showNullDbError
import io.github.yamin8000.cafe.util.Utility.Alerts.snack
import io.github.yamin8000.cafe.util.Utility.Hashes.bCrypt
import io.github.yamin8000.cafe.util.Utility.hideKeyboard
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewAccountFragment :
    CreateUpdateFragment<Account, NewAccountFragmentBinding>(
        Account(),
        { NewAccountFragmentBinding.inflate(it) }
    ) {

    private lateinit var oldPassword: String

    override fun init() {
        oldPassword = item.password
    }

    override fun initViewForCreateMode() {
        lifecycleScope.launch { handleAccountPermission() }
    }

    private suspend fun handleAccountPermission() {
        val accounts = withContext(ioScope.coroutineContext) {
            db?.accountDao()?.getAll()
        }
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
        if (item.id != NO_ID_LONG) {
            binding.accountUserLoginEdit.setText(item.username)
            binding.accountPassLoginEdit.setText(item.password)
            binding.newAccountPermissionEdit.setText(item.permission.toString())
        }
    }

    override suspend fun createItem() {
        val id = withContext(ioScope.coroutineContext) {
            db?.accountDao()?.insert(item)
        }
        if (id != null) accountAddSuccess()
        else showNullDbError()
    }

    override suspend fun editItem() {
        if (item.id != NO_ID_LONG) {
            withContext(ioScope.coroutineContext) {
                db?.accountDao()?.update(item)
            }
            snack(getString(R.string.item_edit_success, getString(R.string.account)))
            hideKeyboard()
        }
    }

    override fun validator(): Boolean {
        return item.username.isNotBlank() && item.password.isNotBlank()
    }

    override fun confirm() {
        binding.newAccountSubmit.setOnClickListener {
            item.username = binding.accountUserLoginEdit.text.toString()
            item.password = handleNewPassword()
            confirmListener(this::validator)
        }
    }

    private fun handleNewPassword(): String {
        return if (binding.accountPassLoginEdit.text.toString() == oldPassword) oldPassword
        else binding.accountPassLoginEdit.text.toString().bCrypt()
    }

    private fun accountAddSuccess() {
        snack(getString(R.string.item_add_success, getString(R.string.account)))
        clear()
    }

    private fun clear() {
        hideKeyboard()
        binding.accountUserLoginEdit.text?.clear()
        binding.accountPassLoginEdit.text?.clear()
    }
}