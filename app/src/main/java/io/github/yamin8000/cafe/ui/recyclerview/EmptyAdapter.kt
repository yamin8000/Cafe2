/*
 *     Cafe/Cafe.app.main
 *     EmptyAdapter.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     EmptyAdapter.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe.ui.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.yamin8000.cafe.R

class EmptyAdapter(
    private val message: String? = null
) : RecyclerView.Adapter<EmptyAdapter.EmptyViewHolder>() {

    class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmptyViewHolder {
        val view = inflateView(parent)
        if (!message.isNullOrBlank())
            setMessage(view.findViewById(R.id.empty_adapter_text))
        return EmptyViewHolder(view)
    }

    private fun inflateView(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.empty_adapter, parent, false)
    }

    private fun setMessage(textView: TextView) {
        textView.text = message
    }

    override fun onBindViewHolder(holder: EmptyViewHolder, position: Int) {
        /* no-op */
    }

    override fun getItemCount() = 1
}