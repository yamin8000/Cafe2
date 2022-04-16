package io.github.yamin8000.cafe.base

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.allViews
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentHomeBinding
import io.github.yamin8000.cafe.db.entities.account.Account
import io.github.yamin8000.cafe.db.entities.account.AccountPermission
import io.github.yamin8000.cafe.ui.util.BaseFragment
import io.github.yamin8000.cafe.util.Constants.ACCOUNT
import io.github.yamin8000.cafe.util.Constants.CRUD_NAME
import io.github.yamin8000.cafe.util.Constants.CURRENT_ACCOUNT_ID
import io.github.yamin8000.cafe.util.Constants.LOGIN
import io.github.yamin8000.cafe.util.Constants.PROMPT
import io.github.yamin8000.cafe.util.Constants.PROMPT_RESULT
import io.github.yamin8000.cafe.util.Constants.PROMPT_TEXT
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Constants.sharedPrefs
import io.github.yamin8000.cafe.util.Utility.Alerts.snack
import io.github.yamin8000.cafe.util.Utility.Views.gone
import io.github.yamin8000.cafe.util.Utility.Views.visible
import io.github.yamin8000.cafe.util.Utility.getCurrentPermission
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
            showPermittedButtons()
            lifecycleScope.launch { initWelcomeText() }
            binding.listOrderButton.setOnClickListener { navigate(R.id.action_homeFragment_to_searchOrdersFragment) }
            binding.newOrderButton.setOnClickListener { navigate(R.id.action_homeFragment_to_newOrderFragment) }
            productsButtonHandler()
            categoriesButtonHandler()
            subscribersButtonHandler()
            binding.accountLoginButton.setOnClickListener { loginHandler() }
            workerButtonHandler()
            paymentsButtonHandler()
            accountButtonHandler()
            reportsButtonHandler()
            scheduleButtonHandler()
            backPressHandler()
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    private fun showPermittedButtons() {
        val buttons = binding.buttonsFlow.allViews
        when (getCurrentPermission()) {
            AccountPermission.Superuser.rank -> buttons.forEach { it.visible() }
            AccountPermission.OrderUser.rank -> {
                buttons.showPermittedButtons(
                    getString(R.string.orders),
                    getString(R.string.new_order),
                    getString(R.string.products),
                    getString(R.string.categories),
                    getString(R.string.subscribers)
                )
            }
            AccountPermission.ScheduleUser.rank -> buttons.showPermittedButtons(getString(R.string.schedule))
            AccountPermission.ReportUser.rank -> buttons.showPermittedButtons(getString(R.string.reports))
            AccountPermission.FinanceUser.rank -> buttons.showPermittedButtons(getString(R.string.payments))
        }
    }

    private suspend fun initWelcomeText() {
        val userId = getUserId()
        val text = if (userId == 0L) getString(R.string.welcome, getString(R.string.guest))
        else getString(R.string.welcome, getUserName(userId))
        binding.welcomeText.text = text
    }

    private fun getUserId() = sharedPrefs?.prefs?.getLong(CURRENT_ACCOUNT_ID, 0) ?: 0

    private suspend fun getUserName(userId: Long): String {
        return db.accountDao().getById(userId)?.username ?: getString(R.string.guest)
    }

    private fun subscribersButtonHandler() {
        binding.subscriberButton.setOnClickListener {
            navigate(
                R.id.action_homeFragment_to_subscriber_graph,
                bundleOf(CRUD_NAME to binding.subscriberButton.text)
            )
        }
    }

    private fun categoriesButtonHandler() {
        binding.categoriesButton.setOnClickListener {
            navigate(
                R.id.action_homeFragment_to_categoryFragment,
                bundleOf(CRUD_NAME to binding.categoriesButton.text)
            )
        }
    }

    private fun productsButtonHandler() {
        binding.productsButton.setOnClickListener {
            navigate(
                R.id.action_homeFragment_to_productFragment,
                bundleOf(CRUD_NAME to binding.productsButton.text)
            )
        }
    }

    private fun scheduleButtonHandler() {
        binding.scheduleButton.setOnClickListener {
            navigateWithPermission(
                R.id.action_homeFragment_to_schedule_graph,
                AccountPermission.ScheduleUser,
                AccountPermission.Superuser,
                bundle = bundleOf(CRUD_NAME to binding.scheduleButton.text)
            )
        }
    }

    private fun paymentsButtonHandler() {
        binding.paymentsButton.setOnClickListener {
            navigateWithPermission(
                R.id.action_homeFragment_to_payment_graph,
                AccountPermission.Superuser,
                AccountPermission.FinanceUser,
                bundle = bundleOf(CRUD_NAME to binding.paymentsButton.text)
            )
        }
    }

    private fun workerButtonHandler() {
        binding.workersButton.setOnClickListener {
            navigateWithPermission(
                R.id.action_homeFragment_to_worker_graph,
                AccountPermission.Superuser,
                bundle = bundleOf(CRUD_NAME to binding.workersButton.text)
            )
        }
    }

    private fun reportsButtonHandler() {
        binding.reportsButton.setOnClickListener {
            navigateWithPermission(
                R.id.action_homeFragment_to_reportFragment,
                AccountPermission.Superuser,
                AccountPermission.ReportUser
            )
        }
    }

    private fun loginHandler() {
        navigate(R.id.action_homeFragment_to_accountLoginModal)
        setFragmentResultListener(LOGIN) { _, bundle ->
            val account = bundle.getParcelable<Account>(ACCOUNT)
            if (account != null) loginSuccess(account.username)
            else snack(getString(R.string.login_failed))
        }
    }

    private fun loginSuccess(username: String) {
        snack(getString(R.string.login_success, username))
        lifecycleScope.launch { initWelcomeText() }
        showPermittedButtons()
    }

    private fun accountButtonHandler() {
        binding.accountButton.setOnClickListener {
            lifecycleScope.launch {
                val accounts = withContext(ioScope.coroutineContext) { db.accountDao().getAll() }
                if (accounts.isNullOrEmpty()) navigate(R.id.action_homeFragment_to_account_graph)
                else {
                    navigateWithPermission(
                        R.id.action_homeFragment_to_account_graph,
                        AccountPermission.Superuser,
                        bundle = bundleOf(CRUD_NAME to binding.accountButton.text)
                    )
                }
            }
        }
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
        vararg targetPermissions: AccountPermission,
        bundle: Bundle? = null
    ) {
        context?.let {
            if (getCurrentPermission() in targetPermissions.map { permission -> permission.rank })
                navigate(destination, bundle)
            else snack(getString(R.string.permission_denied))
        }
    }

    private fun <T : View> Sequence<T>.showPermittedButtons(
        visibleTag: String,
        vararg visibleTags: String
    ) {
        val tags = listOf(visibleTag, *visibleTags)
        this.partition { it.tag in tags }.let { parted ->
            parted.first.forEach { it.visible() }
            parted.second.forEach { it.gone() }
        }
    }
}