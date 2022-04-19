package io.github.yamin8000.cafe.report

import android.os.Bundle
import android.view.View
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentReportBinding
import io.github.yamin8000.cafe.ui.recyclerview.EmptyAdapter
import io.github.yamin8000.cafe.ui.util.BaseFragment
import io.github.yamin8000.cafe.util.Utility.handleCrash
import io.github.yamin8000.cafe.util.Utility.navigate

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
        return context?.resources?.getStringArray(R.array.report_values)?.toList() ?: listOf()
    }
}