package io.github.yamin8000.cafe.base

import android.os.Bundle
import android.view.View
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentCrashBinding
import io.github.yamin8000.cafe.ui.util.BaseFragment
import io.github.yamin8000.cafe.util.Constants.STACKTRACE

class CrashFragment : BaseFragment<FragmentCrashBinding>({ FragmentCrashBinding.inflate(it) }) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var toggle = true

        binding.crashText.setOnClickListener {
            if (toggle)
                activity?.finish()
        }
        binding.crashImage.setOnClickListener { activity?.finish() }
        binding.crashText.setOnLongClickListener {
            toggle = false
            binding.crashText.text =
                arguments?.getString(STACKTRACE) ?: getString(R.string.app_crashed)
            true
        }
    }
}