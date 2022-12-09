/*
 *     Cafe/Cafe.app.main
 *     CrudAdapter.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     CrudAdapter.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe.ui.crud

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView

open class CrudAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    private lateinit var viewHolderCreator: (ViewGroup, LayoutInflater) -> VH
    private lateinit var viewHolderBinder: (VH, Int) -> Unit
    private lateinit var itemCountProvider: () -> Int

    open lateinit var asyncList: AsyncListDiffer<T>

    fun initAdapter(
        viewHolderCreator: (ViewGroup, LayoutInflater) -> VH,
        viewHolderBinder: (VH, Int) -> Unit,
        itemCountProvider: () -> Int
    ) {
        this.viewHolderCreator = viewHolderCreator
        this.viewHolderBinder = viewHolderBinder
        this.itemCountProvider = itemCountProvider
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return viewHolderCreator(parent, LayoutInflater.from(parent.context))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        viewHolderBinder(holder, position)
    }

    override fun getItemCount() = itemCountProvider()

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position
}