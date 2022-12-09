/*
 *     Cafe/Cafe.app.main
 *     SharedPrefs.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     SharedPrefs.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe2.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * ## Shared prefs helper class
 *
 * @param context context
 * @param name shared prefs name
 */
@Suppress("unused")
class SharedPrefs(context: Context, name: String) {

    val prefs: SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    fun <T> write(key: String, value: T) {
        when (value) {
            is Int -> writeInt(key, value)
            is String -> writeString(key, value)
            is Boolean -> writeBoolean(key, value)
            is Long -> writeLong(key, value)
            else -> writeString(key, value.toString())
        }
    }

    private fun writeInt(key: String, value: Int) {
        prefs.edit { putInt(key, value) }
    }

    fun readString(key: String, defaultValue: String = ""): String {
        return prefs.getString(key, defaultValue) ?: ""
    }

    private fun writeString(key: String, value: String) = prefs.edit { putString(key, value) }

    fun readBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return prefs.getBoolean(key, defaultValue)
    }

    private fun writeBoolean(key: String, value: Boolean) {
        prefs.edit { putBoolean(key, value) }
    }

    private fun writeLong(key: String, value: Long) = prefs.edit { putLong(key, value) }

    fun clearData() = prefs.edit().clear().apply()
}