package io.github.yamin8000.cafe.db.entities

import io.github.yamin8000.cafe.util.Constants.NOT_CREATED_ID

class NotCreatedDelegate(private val id: Long) : NotCreated {

    override fun isCreated(): Boolean {
        return id != NOT_CREATED_ID
    }
}