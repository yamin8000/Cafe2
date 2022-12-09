/*
 *     Cafe/Cafe.app.main
 *     SimpleDelayedTextWatcher.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     SimpleDelayedTextWatcher.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe.ui

import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher

class SimpleDelayedTextWatcher(
    private val delay: Long,
    doAfterTextChanged: () -> Unit
) : TextWatcher {

    private val magic = 1

    private val handler = Handler(Looper.getMainLooper()) {
        if (it.what == magic) doAfterTextChanged()
        true
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        //ignored
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        //ignored
    }

    override fun afterTextChanged(s: Editable?) {
        handler.removeMessages(magic)
        handler.sendEmptyMessageDelayed(magic, delay)
    }
}