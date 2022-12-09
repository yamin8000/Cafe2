/*
 *     Cafe/Cafe.app.main
 *     ReportFragment.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     ReportFragment.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe2.report

import android.os.Bundle
import android.view.View
import io.github.yamin8000.cafe2.R
import io.github.yamin8000.cafe2.databinding.FragmentReportBinding
import io.github.yamin8000.cafe2.ui.recyclerview.EmptyAdapter
import io.github.yamin8000.cafe2.ui.util.BaseFragment
import io.github.yamin8000.cafe2.util.Utility.handleCrash
import io.github.yamin8000.cafe2.util.Utility.navigate

class ReportFragment : BaseFragment<FragmentReportBinding>({ FragmentReportBinding.inflate(it) }) {

    private val reports by lazy(LazyThreadSafetyMode.NONE) { fetchReports() }

    private val emptyAdapter by lazy(LazyThreadSafetyMode.NONE) { EmptyAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            handleReportsList()
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    private fun handleReportsList() {
        if (reports.isEmpty()) binding.reportList.adapter = emptyAdapter
        else binding.reportList.adapter = ReportAdapter(reports, this::reportClickListener)
    }

    private fun reportClickListener(report: String) {
        val filterDestination = when (report) {
            getString(R.string.orders) -> R.id.action_reportFragment_to_fragmentOrderReport
            getString(R.string.products) -> R.id.action_reportFragment_to_fragmentProductReport
            else -> return
        }
        navigate(filterDestination)
    }

    private fun fetchReports(): List<String> {
        return context?.resources?.getStringArray(R.array.main_menu_buttons)?.toList() ?: listOf()
    }
}