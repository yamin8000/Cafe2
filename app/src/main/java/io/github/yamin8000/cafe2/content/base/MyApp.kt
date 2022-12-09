/*
 *     Cafe/Cafe.app.main
 *     MyApp.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     MyApp.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe2.content.base

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.room.Room
import io.github.yamin8000.cafe2.db.AppDatabase
import io.github.yamin8000.cafe2.util.Constants
import io.github.yamin8000.cafe2.util.Constants.db
import io.github.yamin8000.cafe2.util.Constants.sharedPrefs
import io.github.yamin8000.cafe2.util.SharedPrefs

class MyApp : Application() {

    @SuppressLint("LogConditional")
    override fun onCreate() {
        super.onCreate()

        try {
            prepareDb()
            prepareSharedPrefs()
        } catch (e: Exception) {
            Log.d(Constants.LOG_TAG, e.stackTraceToString())
        }
    }

    private fun prepareSharedPrefs() {
        sharedPrefs = SharedPrefs(this, this.packageName)
    }

    private fun prepareDb() {
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "db"
        ).build()
    }
}