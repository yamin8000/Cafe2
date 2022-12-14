/*
 *     Cafe/Cafe.app.main
 *     IconHolder.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     IconHolder.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe2.ui.iconpicker

import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import io.github.yamin8000.cafe2.databinding.IconItemBinding

class IconHolder(
    private val drawableIds: List<Int>,
    private val binding: IconItemBinding,
    private val callback: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION)
                callback(drawableIds[adapterPosition])
        }
    }

    fun initView(position: Int) {
        binding.drawableItemImage.setImageDrawable(
            ResourcesCompat.getDrawable(
                binding.root.resources,
                drawableIds[position],
                null
            )
        )
    }
}