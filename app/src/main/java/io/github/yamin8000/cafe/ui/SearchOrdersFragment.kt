package io.github.yamin8000.cafe.ui

import android.os.Bundle
import android.view.View
import io.github.yamin8000.cafe.databinding.FragmentSearchOrdersBinding
import io.github.yamin8000.cafe.ui.util.BaseFragment
import io.github.yamin8000.cafe.util.Utility.handleCrash

class SearchOrdersFragment :
    BaseFragment<FragmentSearchOrdersBinding>({ FragmentSearchOrdersBinding.inflate(it) }) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {

        } catch (e: Exception) {
            handleCrash(e)
        }
    }
}