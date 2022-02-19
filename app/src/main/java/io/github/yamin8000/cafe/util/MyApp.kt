@file:Suppress("unused")

package io.github.yamin8000.cafe.util

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.room.Room
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import io.github.yamin8000.cafe.db.AppDatabase
import io.github.yamin8000.cafe.util.Constants.db

class MyApp : Application() {

    @SuppressLint("LogConditional")
    override fun onCreate() {
        super.onCreate()

        try {
            prepareLogger()
            prepareDb()
        } catch (e: Exception) {
            Log.d(Constants.LOG_TAG, e.stackTraceToString())
        }
    }

    private fun prepareDb() {
        Logger.d("Preparing DB")
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "db"
        ).build()
        Logger.d("DB Prepared!")
    }

    private fun prepareLogger() {
        Logger.addLogAdapter(
            AndroidLogAdapter(
                PrettyFormatStrategy.newBuilder().tag(Constants.LOG_TAG).build()
            )
        )
        Logger.i("Application is Started!")
    }
}