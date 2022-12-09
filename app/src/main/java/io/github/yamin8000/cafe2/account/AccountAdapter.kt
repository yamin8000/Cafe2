/*
 *     Cafe/Cafe.app.main
 *     AccountAdapter.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     AccountAdapter.kt Last modified at 2022/12/9
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

import io.github.yamin8000.cafe2.databinding.AccountItemBinding
import io.github.yamin8000.cafe2.db.entities.account.Account
import io.github.yamin8000.cafe2.ui.crud.CrudAdapter
import io.github.yamin8000.cafe2.ui.recyclerview.AsyncDifferHelper.getAsyncDiffer

class AccountAdapter(
    private val updateCallback: (Account) -> Unit,
    private val deleteCallback: (Account, Boolean) -> Unit
) : CrudAdapter<Account, AccountHolder>() {

    override var asyncList = this.getAsyncDiffer<Account, AccountHolder>(
        { old, new -> old.id == new.id },
        { old, new -> old == new }
    )

    init {
        initAdapter({ parent, inflater ->
            val binding = AccountItemBinding.inflate(inflater, parent, false)
            AccountHolder(asyncList, binding, updateCallback, deleteCallback)
        }, { holder, position ->
            holder.bind(asyncList.currentList[position])
        }, { asyncList.currentList.size })
    }
}