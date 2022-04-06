package io.github.yamin8000.cafe.report

import android.os.Bundle
import android.view.View
import io.github.yamin8000.cafe.databinding.FragmentReportBinding
import io.github.yamin8000.cafe.ui.util.BaseFragment
import io.github.yamin8000.cafe.util.Utility.handleCrash

class ReportFragment : BaseFragment<FragmentReportBinding>({ FragmentReportBinding.inflate(it) }) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            //ignored
        } catch (e: Exception) {
            handleCrash(e)
        }
    }
}