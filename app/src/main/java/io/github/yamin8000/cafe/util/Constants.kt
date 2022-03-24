package io.github.yamin8000.cafe.util

import io.github.yamin8000.cafe.db.AppDatabase

object Constants {

    const val LOG_TAG = "<-->"
    const val STACKTRACE = "stacktrace"
    const val SPLASH_DELAY = 1500L
    var db: AppDatabase? = null
    const val PACK = "pack_"

    //arguments
    const val CATEGORY_NAME = "category_name"
    const val CATEGORY_IMAGE_ID = "category_image_id"
    const val PROMPT = "prompt"
    const val PROMPT_TEXT = "prompt_text"
    const val PROMPT_POSITIVE = "prompt_positive"
    const val PROMPT_NEGATIVE = "prompt_negative"
    const val PROMPT_RESULT = "prompt_result"
}