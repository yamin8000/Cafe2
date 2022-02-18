package io.github.yamin8000.cafe.ui

import android.os.Bundle
import android.view.View
import io.github.yamin8000.cafe.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>({ FragmentHomeBinding.inflate(it) }),
    BaseFragmentBundle {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            if (savedInstanceState != null) handleOld(savedInstanceState)
            else handleNew()
        } catch (e: Exception) {

        }
    }

    override fun handleNew() {
        TODO("Not yet implemented")
    }

    override fun handleOld(bundle: Bundle?) {
        TODO("Not yet implemented")
    }
}