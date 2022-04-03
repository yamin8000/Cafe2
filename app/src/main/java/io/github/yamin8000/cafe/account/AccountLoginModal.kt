package io.github.yamin8000.cafe.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.AccountLoginBinding
import io.github.yamin8000.cafe.db.entities.account.Account
import io.github.yamin8000.cafe.db.entities.account.AccountPermission
import io.github.yamin8000.cafe.util.Constants.ACCOUNT
import io.github.yamin8000.cafe.util.Constants.CURRENT_ACCOUNT_ID
import io.github.yamin8000.cafe.util.Constants.CURRENT_ACCOUNT_TYPE
import io.github.yamin8000.cafe.util.Constants.LOGIN
import io.github.yamin8000.cafe.util.Constants.MASTER
import io.github.yamin8000.cafe.util.Constants.USERNAME
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.SharedPrefs
import io.github.yamin8000.cafe.util.Utility.Alerts.snack
import io.github.yamin8000.cafe.util.Utility.Hashes.isBCryptOf
import io.github.yamin8000.cafe.util.Utility.handleCrash
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountLoginModal : BottomSheetDialogFragment() {

    private val ioScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.IO) }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        AccountLoginBinding.inflate(LayoutInflater.from(context))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            loginSubmitClickListener()
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    private fun loginSubmitClickListener() {
        binding.accountLoginSubmit.setOnClickListener {
            val username = binding.accountUserLoginEdit.text.toString()
            val password = binding.accountPassLoginEdit.text.toString()
            if (MASTER isBCryptOf username && MASTER isBCryptOf password) {
                unlockSecretMasterMode()
                dismiss()
                return@setOnClickListener
            }
            if (username.isNotEmpty() && password.isNotEmpty())
                lifecycleScope.launch { handleUserPass(username, password) }
            else snack(getString(R.string.enter_all_fields))
        }
    }

    private fun unlockSecretMasterMode() {
        context?.let {
            SharedPrefs(it, it.packageName).prefs.edit {
                putInt(
                    CURRENT_ACCOUNT_TYPE,
                    AccountPermission.Superuser.rank
                )
            }
        }
    }

    private suspend fun handleUserPass(
        username: String,
        password: String
    ) {
        val account = getAccount(username)
        if (account == null) snack(getString(R.string.invalid_username_password))
        else foundAccountHandler(account, password)
    }

    private suspend fun getAccount(username: String) = withContext(ioScope.coroutineContext) {
        db?.accountDao()?.getByParams(USERNAME to username)?.firstOrNull()
    }

    private fun foundAccountHandler(
        account: Account,
        password: String
    ) {
        if (account.password isBCryptOf password) loginSuccessHandler(account)
        else snack(getString(R.string.invalid_username_password))
    }

    private fun loginSuccessHandler(account: Account) {
        context?.let {
            val sharedPrefs = SharedPrefs(it, it.packageName)
            sharedPrefs.write(CURRENT_ACCOUNT_ID, account.id)
            sharedPrefs.write(CURRENT_ACCOUNT_TYPE, account.permission.rank)
            setFragmentResult(LOGIN, bundleOf(ACCOUNT to account))
            dismiss()
        }
    }
}