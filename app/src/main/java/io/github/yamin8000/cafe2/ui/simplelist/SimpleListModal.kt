/*
 *     Cafe/Cafe.app.main
 *     SimpleListModal.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     SimpleListModal.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe2.ui.simplelist

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.yamin8000.cafe2.databinding.SimpleListModalBinding
import io.github.yamin8000.cafe2.ui.recyclerview.EmptyAdapter
import io.github.yamin8000.cafe2.util.Constants.DATA
import io.github.yamin8000.cafe2.util.Utility.handleCrash

class SimpleListModal : BottomSheetDialogFragment() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        SimpleListModalBinding.inflate(LayoutInflater.from(context))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val safeContext = context
        return if (safeContext != null) {
            val dialog = BottomSheetDialog(safeContext, theme)
            dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            dialog
        } else super.onCreateDialog(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            val items = arguments?.getStringArray(DATA) ?: arrayOf()
            if (items.isNotEmpty())
                binding.simpleListModalRecycler.adapter = SimpleListAdapter(items.toList())
            else binding.simpleListModalRecycler.adapter = EmptyAdapter()
        } catch (e: Exception) {
            handleCrash(e)
        }
    }
}