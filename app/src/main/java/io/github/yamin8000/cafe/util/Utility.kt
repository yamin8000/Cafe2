package io.github.yamin8000.cafe.util

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.orhanobut.logger.Logger
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.util.Constants.STACKTRACE

object Utility {

    /**
     * Handle soft crashes, that are suppressed using try-catch
     *
     * @param exception exception that caught
     */
    fun Fragment.handleCrash(exception: Exception) {
        val stackTraceToString = exception.stackTraceToString()
        //log it to logcat
        Logger.d(stackTraceToString)
        //navigate user to a special crash screen
        val bundle = bundleOf(STACKTRACE to stackTraceToString)
        this.findNavController().navigate(R.id.crashFragment, bundle)
    }
}