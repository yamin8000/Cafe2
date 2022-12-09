/*
 *     Cafe/Cafe.app.main
 *     ReportAdapter.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     ReportAdapter.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe.report

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.yamin8000.cafe.databinding.ReportItemBinding

class ReportAdapter(
    private val reports: List<String>,
    private val clickListener: (String) -> Unit
) : RecyclerView.Adapter<ReportAdapter.ReportHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportHolder {
        val binding = ReportItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReportHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: ReportHolder, position: Int) {
        holder.bind(reports[position])
    }

    override fun getItemCount() = reports.size

    class ReportHolder(
        private val binding: ReportItemBinding,
        private val clickListener: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.reportText.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    clickListener(binding.reportText.text.toString())
            }
        }

        fun bind(report: String) {
            binding.reportText.text = report
        }
    }
}