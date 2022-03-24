package io.github.yamin8000.cafe.neworder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.orhanobut.logger.Logger
import io.github.yamin8000.cafe.databinding.SingleNewOrderModalBinding
import io.github.yamin8000.cafe.util.Utility.handleCrash

class SingleNewOrderModal : BottomSheetDialogFragment() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        SingleNewOrderModalBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            binding.singleNewOrderText.text = getDetails()
            binding.newOrderPromptAccept.setOnClickListener { setPrompt(true) }
            binding.newOrderPromptDecline.setOnClickListener { setPrompt(false) }
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    private fun getDetails() = arguments?.getString("details", "") ?: ""

    private fun setPrompt(value: Boolean) {
        val backStackEntry = findNavController().currentBackStackEntry
        if (backStackEntry != null) {
            backStackEntry.savedStateHandle.set("prompt", value)
            findNavController().popBackStack()
        } else Logger.d("empty backstack")
    }
}