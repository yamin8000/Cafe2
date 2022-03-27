package io.github.yamin8000.cafe.base

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentHomeBinding
import io.github.yamin8000.cafe.ui.util.BaseFragment
import io.github.yamin8000.cafe.util.Constants.PROMPT
import io.github.yamin8000.cafe.util.Constants.PROMPT_RESULT
import io.github.yamin8000.cafe.util.Constants.PROMPT_TEXT
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
            binding.subscriberButton.setOnClickListener { navigate(R.id.action_homeFragment_to_subscriber_graph) }
            backPressHandler()
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    private fun backPressHandler() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigate(R.id.promptModal, bundleOf(PROMPT_TEXT to getString(R.string.exit_prompt)))
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)
        setFragmentResultListener(PROMPT) { _, bundle ->
            if (bundle.getBoolean(PROMPT_RESULT)) requireActivity().finish()
        }
    }
}