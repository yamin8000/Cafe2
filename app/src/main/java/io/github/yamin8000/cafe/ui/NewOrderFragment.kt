package io.github.yamin8000.cafe.ui

import android.os.Bundle
import android.view.View
import io.github.yamin8000.cafe.databinding.FragmentNewOrderBinding
import io.github.yamin8000.cafe.ui.util.BaseFragment
import io.github.yamin8000.cafe.util.Utility.handleCrash
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class NewOrderFragment :
    BaseFragment<FragmentNewOrderBinding>({ FragmentNewOrderBinding.inflate(it) }) {

    private val ioScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.IO) }
    private val mainScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.Main) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            
        } catch (e: Exception) {
            handleCrash(e)
        }
    }
}