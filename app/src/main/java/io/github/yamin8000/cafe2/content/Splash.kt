/*
 *     Cafe/Cafe.app.main
 *     Splash.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     Splash.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe2.content

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import com.airbnb.lottie.compose.*
import io.github.yamin8000.cafe2.R
import io.github.yamin8000.cafe2.ui.theme.seed
import io.github.yamin8000.cafe2.util.Constants
import kotlinx.coroutines.delay

@Composable
fun SplashContent(
    onSplashFinished: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(Constants.SPLASH_DELAY)
        onSplashFinished()
    }
    Surface(
        color = seed
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.hot_coffee))
        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = LottieConstants.IterateForever
        )
        LottieAnimation(
            composition = composition,
            progress = { progress },
        )
    }
}