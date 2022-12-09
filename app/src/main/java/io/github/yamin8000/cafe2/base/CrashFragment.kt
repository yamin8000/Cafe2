/*
 *     Cafe/Cafe.app.main
 *     CrashFragment.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     CrashFragment.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe2.base

import android.os.Bundle
import android.view.View
import io.github.yamin8000.cafe2.R
import io.github.yamin8000.cafe2.databinding.FragmentCrashBinding
import io.github.yamin8000.cafe2.ui.util.BaseFragment
import io.github.yamin8000.cafe2.util.Constants.STACKTRACE

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