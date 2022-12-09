/*
 *     Cafe/Cafe.app.main
 *     NewSubscriberFragment.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     NewSubscriberFragment.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe2.subscriber

import android.os.Bundle
import android.view.View
import io.github.yamin8000.cafe2.R
import io.github.yamin8000.cafe2.databinding.FragmentNewSubscriberBinding
import io.github.yamin8000.cafe2.db.entities.subscriber.Subscriber
import io.github.yamin8000.cafe2.ui.crud.CreateUpdateFragment
import io.github.yamin8000.cafe2.util.Constants.db
import kotlinx.coroutines.withContext

class NewSubscriberFragment :
    CreateUpdateFragment<Subscriber, FragmentNewSubscriberBinding>(
        Subscriber(),
        { FragmentNewSubscriberBinding.inflate(it) }
    ) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(binding.newSubscriberConfirm)
    }

    override fun initViewForCreateMode() {
        //
    }

    override fun initViewForEditMode() {
        if (item.isCreated()) {
            binding.subscriberNameEdit.setText(item.name)
            binding.subscriberAddressEdit.setText(item.address)
            binding.subscriberPhoneEdit.setText(item.phone)
        }
    }

    override suspend fun createItem() {
        db.subscriberDao().insert(item)
        addSuccess(getString(R.string.subscriber))
    }

    override suspend fun editItem() {
        if (item.isCreated()) {
            withContext(ioScope.coroutineContext) { db.subscriberDao().update(item) }
            editSuccess(getString(R.string.subscriber))
        }
    }

    override fun validator(): Boolean {
        return binding.subscriberNameEdit.text.toString().isNotBlank() &&
                binding.subscriberPhoneEdit.text.toString().isNotBlank()
    }

    override fun confirm() {
        binding.newSubscriberConfirm.setOnClickListener {
            item.name = binding.subscriberNameEdit.text.toString()
            item.address = binding.subscriberAddressEdit.text.toString()
            item.phone = binding.subscriberPhoneEdit.text.toString()
            confirmItem()
        }
    }

    override fun resetViews() {
        binding.subscriberAddressEdit.text?.clear()
        binding.subscriberNameEdit.text?.clear()
        binding.subscriberPhoneEdit.text?.clear()
    }
}
