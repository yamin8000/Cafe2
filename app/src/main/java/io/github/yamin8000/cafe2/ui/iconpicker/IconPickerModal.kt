/*
 *     Cafe/Cafe.app.main
 *     IconPickerModal.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     IconPickerModal.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe2.ui.iconpicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.yamin8000.cafe2.R
import io.github.yamin8000.cafe2.databinding.IconPickerModalBinding
import io.github.yamin8000.cafe2.ui.iconpicker.Utility.getIconPackIds
import io.github.yamin8000.cafe2.util.Constants.ICON_PICKER
import io.github.yamin8000.cafe2.util.Constants.ICON_PICKER_RESULT
import io.github.yamin8000.cafe2.util.Utility.handleCrash

class IconPickerModal : BottomSheetDialogFragment() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        IconPickerModalBinding.inflate(LayoutInflater.from(context), null, false)
    }

    private val icons by lazy(LazyThreadSafetyMode.NONE) { getIconPackIds() }

    private var categoryImageId = R.drawable.pack_top_view_coffee

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            binding.iconList.adapter = IconAdapter(icons, this::onIconClicked)
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    private fun onIconClicked(id: Int) {
        categoryImageId = id
        setFragmentResult(ICON_PICKER, bundleOf(ICON_PICKER_RESULT to categoryImageId))
        dismiss()
    }
}