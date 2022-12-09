/*
 *     Cafe/Cafe.app.main
 *     MainActivity.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     MainActivity.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe2.content.base

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.yamin8000.cafe2.content.HomeContent
import io.github.yamin8000.cafe2.content.SplashContent
import io.github.yamin8000.cafe2.ui.navigation.Nav
import io.github.yamin8000.cafe2.ui.theme.CafeTheme

class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { Scaffold { MainContent() } }
    }
}

@Composable
private fun MainContent() {
    CafeTheme(
        isDarkTheme = isSystemInDarkTheme(),
        isDynamicColor = false
    ) {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = Nav.Routes.Splash
        ) {
            composable(Nav.Routes.Splash) { SplashContent { navController.navigate(Nav.Routes.Home) } }

            composable(Nav.Routes.Home) { HomeContent {} }
        }
    }
}
