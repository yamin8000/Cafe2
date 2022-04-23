package io.github.yamin8000.cafe.ui.simplelist

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.yamin8000.cafe.databinding.SimpleListModalBinding
import io.github.yamin8000.cafe.ui.recyclerview.EmptyAdapter
import io.github.yamin8000.cafe.util.Constants.DATA
import io.github.yamin8000.cafe.util.Utility.handleCrash

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