/*
 *     Cafe/Cafe.app.main
 *     AccountHolder.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     AccountHolder.kt Last modified at 2022/12/9
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

import androidx.recyclerview.widget.AsyncListDiffer
import io.github.yamin8000.cafe2.databinding.AccountItemBinding
import io.github.yamin8000.cafe2.db.entities.account.Account
import io.github.yamin8000.cafe2.ui.crud.CrudHolder

class AccountHolder(
    asyncList: AsyncListDiffer<Account>,
    binding: AccountItemBinding,
    updateCallback: (Account) -> Unit,
    deleteCallback: (Account, Boolean) -> Unit
) : CrudHolder<Account, AccountItemBinding>(
    asyncList,
    binding,
    updateCallback,
    deleteCallback, { account, _ ->
        binding.accountUsername.text = account.username
        binding.accountPermission.text = account.permission.defineToString(binding.root.context)
    }
)