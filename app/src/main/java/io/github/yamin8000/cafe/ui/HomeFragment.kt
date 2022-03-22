package io.github.yamin8000.cafe.ui

import android.os.Bundle
import android.view.View
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentHomeBinding
import io.github.yamin8000.cafe.ui.util.BaseFragment
import io.github.yamin8000.cafe.util.Utility.handleCrash
import io.github.yamin8000.cafe.util.Utility.navigate

class HomeFragment : BaseFragment<FragmentHomeBinding>({ FragmentHomeBinding.inflate(it) }) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            binding.listOrderButton.setOnClickListener { navigate(R.id.action_homeFragment_to_searchOrdersFragment) }
            binding.newOrderButton.setOnClickListener { navigate(R.id.action_homeFragment_to_newOrderFragment) }
            binding.productsButton.setOnClickListener { navigate(R.id.action_homeFragment_to_productFragment) }
            binding.categoriesButton.setOnClickListener { navigate(R.id.action_homeFragment_to_categoryFragment) }
        } catch (e: Exception) {
            handleCrash(e)
        }
    }
}