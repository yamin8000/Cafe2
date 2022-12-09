/*
 *     Cafe/Cafe.app.main
 *     Constants.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     Constants.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe2.util

import io.github.yamin8000.cafe2.db.AppDatabase

object Constants {

    const val LOG_TAG = "<-->"
    const val STACKTRACE = "stacktrace"
    const val SPLASH_DELAY = 2500L
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