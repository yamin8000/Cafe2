/*
 *     Cafe/Cafe.app.main
 *     SimpleListAdapter.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     SimpleListAdapter.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe2.ui.simplelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.yamin8000.cafe2.databinding.SimpleListItemBinding

class SimpleListAdapter(
    private val items: List<String>,
    private val itemClickListener: ((String, Int) -> Unit)? = null
) : RecyclerView.Adapter<SimpleListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleListHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SimpleListItemBinding.inflate(inflater, parent, false)
        return SimpleListHolder(items, binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: SimpleListHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}