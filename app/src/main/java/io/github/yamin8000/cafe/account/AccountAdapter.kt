package io.github.yamin8000.cafe.account

import io.github.yamin8000.cafe.databinding.AccountItemBinding
import io.github.yamin8000.cafe.db.entities.account.Account
import io.github.yamin8000.cafe.ui.crud.CrudAdapter
import io.github.yamin8000.cafe.ui.recyclerview.AsyncDifferHelper.getAsyncDiffer

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