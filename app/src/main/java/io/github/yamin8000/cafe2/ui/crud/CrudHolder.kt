/*
 *     Cafe/Cafe.app.main
 *     CrudHolder.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     CrudHolder.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe2.ui.crud

import android.content.Context
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.google.android.material.card.MaterialCardView

open class CrudHolder<T, VB : ViewBinding>(
    private val asyncList: AsyncListDiffer<T>,
    protected val binding: VB,
    protected val updateCallback: (T) -> Unit,
    protected val deleteCallback: (T, Boolean) -> Unit,
    private val bindView: (T, Context) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val card = binding.root as MaterialCardView

    init {
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.root.setOnClickListener { shortClickListener() }
        binding.root.setOnLongClickListener { longClickListener() }
    }

    private fun shortClickListener() {
        if (adapterPosition != RecyclerView.NO_POSITION && !card.isChecked) {
            updateCallback(asyncList.currentList[adapterPosition])
        }
    }

    private fun longClickListener(): Boolean {
        if (adapterPosition != RecyclerView.NO_POSITION) {
            card.isChecked = !card.isChecked
            deleteCallback(asyncList.currentList[adapterPosition], card.isChecked)
        }
        return true
    }

    fun bind(item: T) {
        bindView(item, binding.root.context)
    }
}