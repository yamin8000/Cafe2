package io.github.yamin8000.cafe.util

import io.github.yamin8000.cafe.db.AppDatabase

object Constants {

    const val LOG_TAG = "<-->"
    const val STACKTRACE = "stacktrace"
    const val SPLASH_DELAY = 1500L
    var db: AppDatabase? = null
    const val PACK = "pack_"
    const val NO_ID = -1
    const val NO_ID_LONG = 0L

    //arguments
    const val IS_EDIT_MODE = "is_edit_mode"
    const val DATA = "data"

    //prompt
    const val PROMPT = "prompt"
    const val PROMPT_TEXT = "prompt_text"
    const val PROMPT_POSITIVE = "prompt_positive"
    const val PROMPT_NEGATIVE = "prompt_negative"
    const val PROMPT_RESULT = "prompt_result"

    //icon picker
    const val ICON_PICKER = "icon_picker"
    const val ICON_PICKER_RESULT = "icon_picker_result"
}