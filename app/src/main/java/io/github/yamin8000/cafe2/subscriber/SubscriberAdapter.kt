/*
 *     Cafe/Cafe.app.main
 *     SubscriberAdapter.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     SubscriberAdapter.kt Last modified at 2022/12/9
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

import io.github.yamin8000.cafe2.databinding.SubscriberItemBinding
import io.github.yamin8000.cafe2.db.entities.subscriber.Subscriber
import io.github.yamin8000.cafe2.ui.recyclerview.AsyncDifferHelper.getAsyncDiffer
import io.github.yamin8000.cafe2.ui.crud.CrudAdapter

class SubscriberAdapter(
    private val updateCallback: (Subscriber) -> Unit,
    private val deleteCallback: (Subscriber, Boolean) -> Unit
) : CrudAdapter<Subscriber, SubscriberHolder>() {

    override var asyncList = this.getAsyncDiffer<Subscriber, SubscriberHolder>(
        { old, new -> old.id == new.id },
        { old, new -> old == new }
    )

    init {
        initAdapter({ parent, inflater ->
            val binding = SubscriberItemBinding.inflate(inflater, parent, false)
            SubscriberHolder(asyncList, binding, updateCallback, deleteCallback)
        }, { holder, position ->
            holder.bind(asyncList.currentList[position])
        }, { asyncList.currentList.size })
    }
}