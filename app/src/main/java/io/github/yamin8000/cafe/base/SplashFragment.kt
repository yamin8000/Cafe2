package io.github.yamin8000.cafe.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.os.postDelayed
import androidx.navigation.fragment.findNavController
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentSplashBinding
import io.github.yamin8000.cafe.ui.util.BaseFragment
import io.github.yamin8000.cafe.util.Constants.SPLASH_DELAY
import io.github.yamin8000.cafe.util.Utility.handleCrash

class SplashFragment : BaseFragment<FragmentSplashBinding>({ FragmentSplashBinding.inflate(it) }) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            Handler(Looper.getMainLooper()).postDelayed(SPLASH_DELAY) {
                if (!this.isDetached)
                    findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
            }
        } catch (exception: Exception) {
            handleCrash(exception)
        }
    }
}