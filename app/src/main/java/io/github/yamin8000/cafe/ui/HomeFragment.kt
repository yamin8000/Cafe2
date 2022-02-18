package io.github.yamin8000.cafe.ui

import android.os.Bundle
import android.view.View
import io.github.yamin8000.cafe.databinding.FragmentHomeBinding
import io.github.yamin8000.cafe.ui.util.BaseFragment
import io.github.yamin8000.cafe.ui.util.BaseFragmentBundle
import io.github.yamin8000.cafe.util.Utility.handleCrash

class HomeFragment :
    BaseFragment<FragmentHomeBinding>({ FragmentHomeBinding.inflate(it) }),
    BaseFragmentBundle {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            if (savedInstanceState != null) handleOld(savedInstanceState)
            else handleNew()
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    override fun handleNew() {
        //TODO("Not yet implemented")
    }

    override fun handleOld(bundle: Bundle?) {
        //TODO("Not yet implemented")
    }
}