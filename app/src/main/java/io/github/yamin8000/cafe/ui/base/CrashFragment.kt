package io.github.yamin8000.cafe.ui.base

import android.os.Bundle
import android.view.View
import io.github.yamin8000.cafe.databinding.FragmentCrashBinding
import io.github.yamin8000.cafe.ui.util.BaseFragment

class CrashFragment : BaseFragment<FragmentCrashBinding>({ FragmentCrashBinding.inflate(it) }) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.crashText.setOnClickListener { activity?.finish() }
    }
}