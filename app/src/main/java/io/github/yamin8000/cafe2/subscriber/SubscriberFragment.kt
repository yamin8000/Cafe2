/*
 *     Cafe/Cafe.app.main
 *     SubscriberFragment.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     SubscriberFragment.kt Last modified at 2022/12/9
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

import androidx.recyclerview.widget.GridLayoutManager
import io.github.yamin8000.cafe2.R
import io.github.yamin8000.cafe2.db.entities.subscriber.Subscriber
import io.github.yamin8000.cafe2.ui.crud.ReadDeleteFragment
import io.github.yamin8000.cafe2.util.Constants.db

class SubscriberFragment : ReadDeleteFragment<Subscriber, SubscriberHolder>(R.id.newSubscriberFragment) {

    override suspend fun getItems(): List<Subscriber> {
        return db.subscriberDao().getAll()
    }

    override suspend fun dbDeleteAction() {
        db.subscriberDao().deleteAll(deleteCandidates)
    }

    override fun fillList() {
        val adapter = SubscriberAdapter(this::updateCallback, this::deleteCallback)
        binding.crudList.adapter = adapter
        binding.crudList.layoutManager = GridLayoutManager(context, 2)
        adapter.asyncList.submitList(items)
    }
}