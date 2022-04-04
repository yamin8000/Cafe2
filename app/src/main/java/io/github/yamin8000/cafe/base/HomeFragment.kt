package io.github.yamin8000.cafe.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentHomeBinding
import io.github.yamin8000.cafe.db.entities.account.Account
import io.github.yamin8000.cafe.db.entities.account.AccountPermission
import io.github.yamin8000.cafe.ui.util.BaseFragment
import io.github.yamin8000.cafe.util.Constants.ACCOUNT
import io.github.yamin8000.cafe.util.Constants.CURRENT_ACCOUNT_TYPE
import io.github.yamin8000.cafe.util.Constants.LOGIN
import io.github.yamin8000.cafe.util.Constants.NO_ID
import io.github.yamin8000.cafe.util.Constants.PROMPT
import io.github.yamin8000.cafe.util.Constants.PROMPT_RESULT
import io.github.yamin8000.cafe.util.Constants.PROMPT_TEXT
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.SharedPrefs
import io.github.yamin8000.cafe.util.Utility.Alerts.snack
import io.github.yamin8000.cafe.util.Utility.handleCrash
import io.github.yamin8000.cafe.util.Utility.navigate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : BaseFragment<FragmentHomeBinding>({ FragmentHomeBinding.inflate(it) }) {

    private val ioScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.IO) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            binding.listOrderButton.setOnClickListener { navigate(R.id.action_homeFragment_to_searchOrdersFragment) }
            binding.newOrderButton.setOnClickListener { navigate(R.id.action_homeFragment_to_newOrderFragment) }
            binding.productsButton.setOnClickListener { navigate(R.id.action_homeFragment_to_productFragment) }
            binding.categoriesButton.setOnClickListener { navigate(R.id.action_homeFragment_to_categoryFragment) }
            binding.subscriberButton.setOnClickListener { navigate(R.id.action_homeFragment_to_subscriber_graph) }
            binding.accountLoginButton.setOnClickListener { loginHandler() }
            binding.workersButton.setOnClickListener { navigate(R.id.action_homeFragment_to_worker_graph) }
            binding.paymentsButton.setOnClickListener { navigate(R.id.action_homeFragment_to_payment_graph) }
            accountButtonHandler()
            reportsButtonHandler()
            backPressHandler()
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    private fun reportsButtonHandler() {
        //navigateWithPermission(0, AccountPermission.Superuser)
    }

    private fun loginHandler() {
        navigate(R.id.action_homeFragment_to_accountLoginModal)
        setFragmentResultListener(LOGIN) { _, bundle ->
            val account = bundle.getParcelable<Account>(ACCOUNT)
            if (account != null) snack(getString(R.string.login_success, account.username))
            else snack(getString(R.string.login_failed))
        }
    }

    private fun accountButtonHandler() {
        binding.accountButton.setOnClickListener {
            lifecycleScope.launch {
                val accounts = withContext(ioScope.coroutineContext) { db?.accountDao()?.getAll() }
                if (accounts.isNullOrEmpty()) navigate(R.id.action_homeFragment_to_account_graph)
                else {
                    navigateWithPermission(
                        R.id.action_homeFragment_to_account_graph,
                        AccountPermission.Superuser
                    )
                }
            }
        }
    }

    private fun getCurrentPermission(it: Context): Int {
        return SharedPrefs(it, it.packageName).prefs.getInt(
            CURRENT_ACCOUNT_TYPE,
            NO_ID
        )
    }

    private fun backPressHandler() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigate(R.id.promptModal, bundleOf(PROMPT_TEXT to getString(R.string.exit_prompt)))
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)
        setFragmentResultListener(PROMPT) { _, bundle ->
            if (bundle.getBoolean(PROMPT_RESULT)) requireActivity().finish()
        }
    }

    private fun navigateWithPermission(
        destination: Int,
        targetPermission: AccountPermission
    ) {
        context?.let {
            if (getCurrentPermission(it) == targetPermission.rank)
                navigate(destination)
            else snack(getString(R.string.permission_denied))
        }
    }
}