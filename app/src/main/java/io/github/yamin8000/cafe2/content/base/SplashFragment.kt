/*
 *     Cafe/Cafe.app.main
 *     SplashFragment.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     SplashFragment.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe2.content.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.os.postDelayed
import androidx.navigation.fragment.findNavController
import io.github.yamin8000.cafe2.R
import io.github.yamin8000.cafe2.databinding.FragmentSplashBinding
import io.github.yamin8000.cafe2.ui.util.BaseFragment
import io.github.yamin8000.cafe2.util.Constants.SPLASH_DELAY
import io.github.yamin8000.cafe2.util.Utility.handleCrash

class SplashFragment : BaseFragment<FragmentSplashBinding>({ FragmentSplashBinding.inflate(it) }) {

    private lateinit var handler: Handler
    private var isSplashShown = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            handler = Handler(Looper.getMainLooper())
            handler.postDelayed(SPLASH_DELAY) {
                navigateToSearchFragment()
            }
        } catch (exception: Exception) {
            handleCrash(exception)
        }
    }

    override fun onPause() {
        super.onPause()

        isSplashShown = true
        if (this::handler.isInitialized)
            handler.removeCallbacksAndMessages(null)
    }

    override fun onResume() {
        super.onResume()
        if (isSplashShown)
            navigateToSearchFragment()
    }

    private fun navigateToSearchFragment() {
        if (!this.isDetached) {
            findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
        }
    }
}