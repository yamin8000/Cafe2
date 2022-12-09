/*
 *     Cafe/Cafe.app.main
 *     AccountFragment.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     AccountFragment.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe.account

import androidx.core.content.edit
import androidx.recyclerview.widget.GridLayoutManager
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.db.entities.account.Account
import io.github.yamin8000.cafe.ui.crud.ReadDeleteFragment
import io.github.yamin8000.cafe.util.Constants.CURRENT_ACCOUNT_ID
import io.github.yamin8000.cafe.util.Constants.CURRENT_ACCOUNT_TYPE
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.SharedPrefs
import io.github.yamin8000.cafe.util.Utility.Alerts.snack
import kotlinx.coroutines.withContext

class AccountFragment : ReadDeleteFragment<Account, AccountHolder>(R.id.newAccountFragment) {

    override suspend fun getItems(): List<Account> {
        return withContext(ioScope.coroutineContext) {
            db.accountDao().getAll()
        }
    }

    override suspend fun dbDeleteAction() {
        deleteUserFromSharedPrefsIfNeeded()
        withContext(ioScope.coroutineContext) {
            db.accountDao().deleteAll(deleteCandidates)
        }
        snack(getString(R.string.item_delete_success, getString(R.string.account)))
    }

    private fun deleteUserFromSharedPrefsIfNeeded() {
        context?.let {
            val prefs = SharedPrefs(it, it.packageName).prefs
            val savedId = prefs.getLong(CURRENT_ACCOUNT_ID, -1)
            val isDeletingCurrentUser = deleteCandidates.any { item -> item.id == savedId }
            if (isDeletingCurrentUser) {
                prefs.edit {
                    remove(CURRENT_ACCOUNT_ID)
                    remove(CURRENT_ACCOUNT_TYPE)
                }
            }
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