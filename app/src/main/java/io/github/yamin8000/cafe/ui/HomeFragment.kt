package io.github.yamin8000.cafe.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentHomeBinding
import io.github.yamin8000.cafe.ui.util.BaseFragment
import io.github.yamin8000.cafe.util.Utility.handleCrash

class HomeFragment : BaseFragment<FragmentHomeBinding>({ FragmentHomeBinding.inflate(it) }) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            binding.listOrderButton.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_searchOrdersFragment) }
            binding.newOrderButton.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_newOrderFragment) }
            binding.productsButton.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_productFragment) }
        } catch (e: Exception) {
            handleCrash(e)
        }
    }
}