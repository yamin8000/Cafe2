package io.github.yamin8000.cafe.util

import io.github.yamin8000.cafe.db.AppDatabase

object Constants {

    const val LOG_TAG = "<-->"
    const val STACKTRACE = "stacktrace"
    const val SPLASH_DELAY = 1500L
    const val PACK = "pack_"
    const val NOT_CREATED_ID = 0L

    const val TEXT_WATCHER_DELAY = 500L

    //singletons
    lateinit var db: AppDatabase
    var sharedPrefs: SharedPrefs? = null

    //master user-pass
    @Suppress("SpellCheckingInspection")
    const val MASTER = "\$2a\$12\$nc3dCjoj6IBhqSteUdir7.WAONMwwBQFNk4OMkiZ.ZHw9xbrnjzuO"

    //arguments
    const val IS_EDIT_MODE = "is_edit_mode"
    const val DATA = "data"
    const val USERNAME = "username"
    const val CURRENT_ACCOUNT_ID = "current_account_id"
    const val CURRENT_ACCOUNT_TYPE = "current_account_type"
    const val LOGIN = "login"
    const val ACCOUNT = "account"
    const val CRUD_NAME = "crud_name"

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