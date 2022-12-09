/*
 *     Cafe/Cafe.app.main
 *     HomeFragment.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     HomeFragment.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe2.content.base

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import io.github.yamin8000.cafe2.R
import io.github.yamin8000.cafe2.databinding.FragmentHomeBinding
import io.github.yamin8000.cafe2.db.entities.account.Account
import io.github.yamin8000.cafe2.db.entities.account.AccountPermission
import io.github.yamin8000.cafe2.ui.util.BaseFragment
import io.github.yamin8000.cafe2.util.Constants.ACCOUNT
import io.github.yamin8000.cafe2.util.Constants.CRUD_NAME
import io.github.yamin8000.cafe2.util.Constants.CURRENT_ACCOUNT_ID
import io.github.yamin8000.cafe2.util.Constants.LOGIN
import io.github.yamin8000.cafe2.util.Constants.PROMPT
import io.github.yamin8000.cafe2.util.Constants.PROMPT_RESULT
import io.github.yamin8000.cafe2.util.Constants.PROMPT_TEXT
import io.github.yamin8000.cafe2.util.Constants.db
import io.github.yamin8000.cafe2.util.Constants.sharedPrefs
import io.github.yamin8000.cafe2.util.Utility.Alerts.snack
import io.github.yamin8000.cafe2.util.Utility.Views.gone
import io.github.yamin8000.cafe2.util.Utility.Views.referencedViews
import io.github.yamin8000.cafe2.util.Utility.Views.visible
import io.github.yamin8000.cafe2.util.Utility.getCurrentPermission
import io.github.yamin8000.cafe2.util.Utility.navigate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : BaseFragment<FragmentHomeBinding>({ FragmentHomeBinding.inflate(it) }) {

    private val ioScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.IO) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            lifecycleScope.launch { initWelcomeText() }
            showPermittedButtons()
            buttonsHandlers()
            backPressHandler()
        } catch (e: Exception) {
            //handleCrash(e)
        }
    }

    private fun buttonsHandlers() {
        binding.listOrderButton.setOnClickListener { navigate(R.id.action_homeFragment_to_searchOrdersFragment) }
        binding.newOrderButton.setOnClickListener { navigate(R.id.action_homeFragment_to_newOrderFragment) }
        productsButtonHandler()
        categoriesButtonHandler()
        subscribersButtonHandler()
        binding.accountLoginButton.setOnClickListener { loginHandler() }
        workerButtonHandler()
        paymentsButtonHandler()
        binding.accountButton.setOnClickListener { lifecycleScope.launch { handleAccountsButton() } }
        reportsButtonHandler()
        scheduleButtonHandler()
    }

    private suspend fun handleAccountsButton() {
        val accounts = withContext(ioScope.coroutineContext) { db.accountDao().getAll() }
        if (accounts.isNullOrEmpty()) navigate(R.id.action_homeFragment_to_newAccountFragment)
        else {
            navigateWithPermission(
                R.id.action_homeFragment_to_accountFragment,
                AccountPermission.Superuser,
                bundle = bundleOf(CRUD_NAME to binding.accountButton.text)
            )
        }
    }

    private fun showPermittedButtons() {
        val buttons = binding.buttonsFlow.referencedViews()
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
        val welcomeText = if (userId == 0L) getString(R.string.welcome, getString(R.string.guest))
        else getString(R.string.welcome, getUserName(userId))
        binding.welcomeText.text = welcomeText
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
        if (getCurrentPermission() == AccountPermission.Guest.rank)
            lifecycleScope.launch { handleFirstAccountCreation() }
        else handleLoginForUsers()

    }

    private fun handleLoginForUsers() {
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

    private suspend fun handleFirstAccountCreation() {
        val accounts = withContext(ioScope.coroutineContext) { db.accountDao().getAll() }
        if (accounts.isNullOrEmpty()) navigate(R.id.action_homeFragment_to_newAccountFragment)
        else handleLoginForUsers()
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

    private fun <T : View> List<T>.showPermittedButtons(
        visibleTag: String,
        vararg visibleTags: String
    ) {
        val tags = listOf(visibleTag, *visibleTags)
        val (visibleViews, goneViews) = this.partition { it.tag in tags }
        visibleViews.forEach { it.visible() }
        goneViews.forEach { it.gone() }
    }
}