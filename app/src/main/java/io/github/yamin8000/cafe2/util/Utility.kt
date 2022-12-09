/*
 *     Cafe/Cafe.app.main
 *     Utility.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     Utility.kt Last modified at 2022/12/9
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

@file:Suppress("unused")

package io.github.yamin8000.cafe2.util

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.helper.widget.Flow
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import at.favre.lib.crypto.bcrypt.BCrypt
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import io.github.yamin8000.cafe2.R
import io.github.yamin8000.cafe2.db.entities.account.AccountPermission
import io.github.yamin8000.cafe2.util.Constants.IS_EDIT_MODE
import io.github.yamin8000.cafe2.util.Constants.STACKTRACE
import io.github.yamin8000.cafe2.util.Constants.sharedPrefs
import java.math.BigInteger
import java.util.*

@Suppress("MemberVisibilityCanBePrivate")
object Utility {

    @Suppress("DEPRECATION")
    fun getCurrentLocale(
        context: Context
    ): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales.get(0)
        } else context.resources.configuration.locale
    }

    operator fun String.div(that: String) = "$this/$that"

    /**
     * Handle soft crashes, that are suppressed using try-catch
     *
     * @param throwable exception that caught
     */
    fun Fragment.handleCrash(throwable: Throwable) {
        val stackTraceToString = throwable.stackTraceToString()
        //navigate user to a special crash screen
        val bundle = bundleOf(STACKTRACE to stackTraceToString)
        //this.navigate(R.id.crashFragment, bundle)
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
            //handleCrash(exception)
        }
    }

    fun Fragment.navigate(destination: Int, args: Bundle? = null) {
        try {
            this.findNavController().navigate(destination, args)
        } catch (exception: Exception) {
            //handleCrash(exception)
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

    fun getCurrentPermission(): Int {
        return sharedPrefs?.prefs?.getInt(
            Constants.CURRENT_ACCOUNT_TYPE,
            AccountPermission.Guest.rank
        ) ?: AccountPermission.Guest.rank
    }

    fun isSuperuser() = getCurrentPermission() == AccountPermission.Superuser.rank

    inline fun <T> csvOf(
        headers: List<String>,
        data: List<T>,
        itemBuilder: (T) -> List<String>
    ) = buildString {
        append(headers.joinToString(",") { "\"$it\"" })
        append("\n")
        data.forEach { item ->
            append(itemBuilder(item).joinToString(",") { "\"$it\"" })
            append("\n")
        }
    }

    object Views {

        fun <T> Context.autoCompleteAdapter(items: List<T>): ArrayAdapter<T> {
            return ArrayAdapter(this, R.layout.dropdown_item, items)
        }

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

        fun TextView.handleData(data: String?) {
            if (!data.isNullOrBlank()) {
                this.visible()
                this.text = data
            } else this.gone()
        }

        fun Flow.referencedViews(): List<View> {
            val views = mutableListOf<View>()
            for (id in this.referencedIds) {
                val view = this.rootView.findViewById<View>(id)
                if (view != null) views.add(view)
            }
            return views
        }

        fun TextInputEditText.handlePrice(): Long? {
            return try {
                this.text.toString().let { if (it.isNotBlank()) it.toLong() else null }
            } catch (e: NumberFormatException) {
                return 0
            }
        }
    }

    object Alerts {

        fun Fragment.toast(message: String, duration: Int = Toast.LENGTH_SHORT): Toast? {
            return try {
                val toast = Toast.makeText(context, message, duration)
                toast.show()
                toast
            } catch (e: Exception) {
                //handleCrash(e)
                null
            }
        }

        fun Fragment.showDbError() {
            snack(getString(R.string.db_general_error))
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