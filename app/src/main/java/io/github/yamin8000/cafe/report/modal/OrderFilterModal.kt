package io.github.yamin8000.cafe.report.modal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.yamin8000.cafe.databinding.OrderFilterModalBinding
import io.github.yamin8000.cafe.util.Constants.FILTER
import io.github.yamin8000.cafe.util.Constants.IS_DELIVERED
import io.github.yamin8000.cafe.util.Utility.handleCrash

class OrderFilterModal : BottomSheetDialogFragment() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        OrderFilterModalBinding.inflate(layoutInflater)
    }

    private var isDelivered: Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            binding.deliveredSwitch.setOnCheckedChangeListener { _, isChecked ->
                isDelivered = isChecked
            }
            binding.orderFilterConfirm.setOnClickListener { sendArguments() }
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    private fun sendArguments() {
        setFragmentResult(FILTER, Bundle().apply {
            isDelivered?.let { putBoolean(IS_DELIVERED, it) }

        })
    }
}