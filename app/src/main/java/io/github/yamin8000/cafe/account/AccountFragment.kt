package io.github.yamin8000.cafe.account

import android.os.Bundle
import android.view.View
import androidx.core.content.edit
import androidx.recyclerview.widget.GridLayoutManager
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.db.entities.account.Account
import io.github.yamin8000.cafe.ui.crud.ReadDeleteFragment
import io.github.yamin8000.cafe.util.Constants.CURRENT_ACCOUNT_ID
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.SharedPrefs
import io.github.yamin8000.cafe.util.Utility.Alerts.snack
import kotlinx.coroutines.withContext

class AccountFragment : ReadDeleteFragment<Account, AccountHolder>(R.id.newAccountFragment) {

    override suspend fun getItems(): List<Account> {
        return withContext(ioScope.coroutineContext) {
            db?.accountDao()?.getAll() ?: emptyList()
        }
    }

    override suspend fun dbDeleteAction() {
        deleteUserFromSharedPrefsIfNeeded()
        withContext(ioScope.coroutineContext) {
            db?.accountDao()?.deleteAll(deleteCandidates)
        }
        snack(getString(R.string.item_delete_success, getString(R.string.account)))
    }

    private fun deleteUserFromSharedPrefsIfNeeded() {
        context?.let {
            val prefs = SharedPrefs(it, it.packageName).prefs
            val savedId = prefs.getLong(CURRENT_ACCOUNT_ID, -1)
            val isDeletingCurrentUser = deleteCandidates.any { item -> item.id == savedId }
            prefs.edit { remove(CURRENT_ACCOUNT_ID) }
        }
    }

    override fun fillList() {
        AccountAdapter(this::updateCallback, this::deleteCallback).let { adapter ->
            binding.crudList.adapter = adapter
            context.let { binding.crudList.layoutManager = GridLayoutManager(it, 2) }
            adapter.asyncList.submitList(items)
        }
    }
}