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