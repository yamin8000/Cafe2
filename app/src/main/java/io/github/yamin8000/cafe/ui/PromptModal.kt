package io.github.yamin8000.cafe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.PromptModalBinding
import io.github.yamin8000.cafe.util.Constants.PROMPT
import io.github.yamin8000.cafe.util.Constants.PROMPT_NEGATIVE
import io.github.yamin8000.cafe.util.Constants.PROMPT_POSITIVE
import io.github.yamin8000.cafe.util.Constants.PROMPT_RESULT
import io.github.yamin8000.cafe.util.Constants.PROMPT_TEXT
import io.github.yamin8000.cafe.util.Utility.handleCrash

class PromptModal : BottomSheetDialogFragment() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        PromptModalBinding.inflate(LayoutInflater.from(context))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            setTextsFromArguments()
            setListeners()
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    private fun setListeners() {
        binding.noticePositiveButton.setOnClickListener {
            setFragmentResult(
                PROMPT,
                bundleOf(PROMPT_RESULT to true)
            )
            dismiss()
        }
        binding.noticeNegativeButton.setOnClickListener {
            setFragmentResult(
                PROMPT,
                bundleOf(PROMPT_RESULT to false)
            )
            dismiss()
        }
    }

    private fun setTextsFromArguments() {
        arguments?.let {
            binding.noticeText.text =
                it.getString(PROMPT_TEXT) ?: getString(R.string.prompt_default)
            binding.noticePositiveButton.text =
                it.getString(PROMPT_POSITIVE) ?: getString(R.string.yes)
            binding.noticeNegativeButton.text =
                it.getString(PROMPT_NEGATIVE) ?: getString(R.string.no)
        }
    }
}