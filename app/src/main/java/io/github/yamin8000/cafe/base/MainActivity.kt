package io.github.yamin8000.cafe.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.yamin8000.cafe.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}