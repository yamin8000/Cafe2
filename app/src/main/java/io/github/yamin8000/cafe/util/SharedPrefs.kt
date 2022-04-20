package io.github.yamin8000.cafe.util

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