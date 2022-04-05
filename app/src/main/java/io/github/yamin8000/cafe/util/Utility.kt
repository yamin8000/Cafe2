package io.github.yamin8000.cafe.util

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import at.favre.lib.crypto.bcrypt.BCrypt
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.orhanobut.logger.Logger
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.db.entities.account.AccountPermission
import io.github.yamin8000.cafe.util.Constants.IS_EDIT_MODE
import io.github.yamin8000.cafe.util.Constants.STACKTRACE
import io.github.yamin8000.cafe.util.Constants.sharedPrefs
import java.math.BigInteger

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
        this.navigate(R.id.crashFragment, bundle)
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
        try {
            this.findNavController().navigate(destination, args)
        } catch (exception: Exception) {
            handleCrash(exception)
        }
    }

    /**
     * Format string number
     *
     * @return number in form of 1,000,000
     */
    fun String?.numFormat(): String {
        if (this == null) return "0"
        return try {
            val number = BigInteger(this)
            String.format("%,d", number)
        } catch (e: Exception) {
            this
        }
    }

    fun Context.getCurrentPermission(): Int {
        return sharedPrefs?.prefs?.getInt(
            Constants.CURRENT_ACCOUNT_TYPE,
            Constants.NO_ID
        ) ?: AccountPermission.Guest.rank
    }

    object Views {

        fun TextInputEditText.getNumber(): Long {
            return try {
                if (this.text.isNullOrBlank()) -1L
                else this.text.toString().toLong()
            } catch (numberFormatException: NumberFormatException) {
                -1L
            }
        }

        fun ImageView.setImageFromResourceId(resourceId: Int) {
            this.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    resourceId,
                    null
                )
            )
        }

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

        fun Fragment.toast(message: String, duration: Int = Toast.LENGTH_SHORT): Toast? {
            return try {
                val toast = Toast.makeText(context, message, duration)
                toast.show()
                toast
            } catch (e: Exception) {
                handleCrash(e)
                null
            }
        }

        fun Fragment.showNullDbError() {
            snack(getString(R.string.db_null_error))
        }

        fun Fragment.snack(
            message: String,
            length: Int = Snackbar.LENGTH_LONG
        ): Snackbar? {
            return try {
                val root = this.view
                if (root != null) createSnack(root, message, length) else null
            } catch (e: Exception) {
                toast(message)
                null
            }
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

    object Hashes {

        fun String.bCrypt(): String {
            return BCrypt.withDefaults().hashToString(12, this.toCharArray())
        }

        infix fun String.isBCryptOf(password: String): Boolean {
            return BCrypt.verifyer().verify(password.toCharArray(), this).verified
        }
    }
}