package io.github.yamin8000.cafe.util

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.orhanobut.logger.Logger
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.util.Constants.IS_EDIT_MODE
import io.github.yamin8000.cafe.util.Constants.STACKTRACE

object Utility {

    /**
     * Handle soft crashes, that are suppressed using try-catch
     *
     * @param throwable exception that caught
     */
    fun Fragment.handleCrash(throwable: Throwable) {
        val stackTraceToString = throwable.stackTraceToString()
        //log it to logcat
        Logger.d(stackTraceToString)
        //navigate user to a special crash screen
        val bundle = bundleOf(STACKTRACE to stackTraceToString)
        this.findNavController().navigate(R.id.crashFragment, bundle)
    }

    /**
     * Hide keyboard inside fragment
     *
     * since this is not my code and looks shady
     *
     * I don't know about any errors that can happen
     *
     * so it's wrapped inside try/catch
     *
     */
    fun Fragment.hideKeyboard() {
        try {
            val activity = this.activity
            if (activity != null) {
                val imm =
                    activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
                var view = activity.currentFocus
                if (view == null) view = View(activity)
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        } catch (exception: Exception) {
            handleCrash(exception)
        }
    }

    fun Fragment.navigate(destination: Int, args: Bundle? = null) {
        this.findNavController().navigate(destination, args)
    }

    object Views {

        fun View.visible() {
            this.visibility = View.VISIBLE
        }

        fun View.gone() {
            this.visibility = View.GONE
        }

        fun View.invisible() {
            this.visibility = View.INVISIBLE
        }
    }

    object Alerts {

        fun Fragment.toast(message: String, duration: Int = Toast.LENGTH_SHORT): Toast {
            val toast = Toast.makeText(context, message, duration)
            toast.show()
            return toast
        }

        fun Fragment.showNullDbError() {
            snack(getString(R.string.db_null_error))
        }

        fun Fragment.snack(
            message: String,
            length: Int = Snackbar.LENGTH_LONG
        ): Snackbar? {
            val root = this.view
            return if (root != null) createSnack(root, message, length) else null
        }

        private fun createSnack(
            root: View,
            message: String,
            length: Int = Snackbar.LENGTH_LONG
        ): Snackbar {
            val snack = Snackbar.make(root, message, length)
            snack.show()
            return snack
        }
    }

    object Bundles {

        fun Bundle?.isEditMode() = this?.getBoolean(IS_EDIT_MODE) ?: false

        fun <T : Parcelable> Bundle?.data(): T? = this?.getParcelable(Constants.DATA)
    }
}