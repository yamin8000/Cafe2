package io.github.yamin8000.cafe.account

import androidx.recyclerview.widget.AsyncListDiffer
import io.github.yamin8000.cafe.databinding.AccountItemBinding
import io.github.yamin8000.cafe.db.entities.account.Account
import io.github.yamin8000.cafe.ui.crud.CrudHolder

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