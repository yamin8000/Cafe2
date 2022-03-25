package io.github.yamin8000.cafe.ui.iconpicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.IconPickerModalBinding
import io.github.yamin8000.cafe.ui.iconpicker.Utility.getIconPackIds
import io.github.yamin8000.cafe.util.Constants.ICON_PICKER
import io.github.yamin8000.cafe.util.Constants.ICON_PICKER_RESULT
import io.github.yamin8000.cafe.util.Utility.handleCrash

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