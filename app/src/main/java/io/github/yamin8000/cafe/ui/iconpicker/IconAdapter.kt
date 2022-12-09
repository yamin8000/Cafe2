/*
 *     Cafe/Cafe.app.main
 *     IconAdapter.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     IconAdapter.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe.ui.iconpicker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.yamin8000.cafe.databinding.IconItemBinding

class IconAdapter(
    private val drawableIds: List<Int>,
    private val callback: (Int) -> Unit
) : RecyclerView.Adapter<IconHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = IconItemBinding.inflate(inflater, parent, false)
        return IconHolder(drawableIds, binding, callback)
    }

    override fun onBindViewHolder(holder: IconHolder, position: Int) {
        holder.initView(position)
    }

    override fun getItemCount() = drawableIds.size

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position
}