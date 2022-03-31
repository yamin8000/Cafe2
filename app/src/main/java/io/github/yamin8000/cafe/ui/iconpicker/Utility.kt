package io.github.yamin8000.cafe.ui.iconpicker

import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.util.Constants

object Utility {

    fun getIconPackIds(): List<Int> {
        return R.drawable::class.java.fields.filter {
            it.name.startsWith(Constants.PACK)
        }.map { it.getInt(null) }
    }
}