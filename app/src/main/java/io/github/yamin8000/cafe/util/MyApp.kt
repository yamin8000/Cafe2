@file:Suppress("unused")

package io.github.yamin8000.cafe.util

import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        prepareLogger()
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