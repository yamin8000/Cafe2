/*
 *     Cafe/Cafe.app.main
 *     PromptModal.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     PromptModal.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe2.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.yamin8000.cafe2.R
import io.github.yamin8000.cafe2.databinding.PromptModalBinding
import io.github.yamin8000.cafe2.util.Constants.PROMPT
import io.github.yamin8000.cafe2.util.Constants.PROMPT_NEGATIVE
import io.github.yamin8000.cafe2.util.Constants.PROMPT_POSITIVE
import io.github.yamin8000.cafe2.util.Constants.PROMPT_RESULT
import io.github.yamin8000.cafe2.util.Constants.PROMPT_TEXT
import io.github.yamin8000.cafe2.util.Utility.handleCrash

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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val safeContext = context
        return if (safeContext != null) {
            val dialog = BottomSheetDialog(safeContext, theme)
            dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            dialog
        } else super.onCreateDialog(savedInstanceState)
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