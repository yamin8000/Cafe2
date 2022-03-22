package io.github.yamin8000.cafe.ui.drawable

import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.util.Constants

object Utility {

    fun Fragment.getIconPack(): List<Pair<Int, Drawable?>> {
        return R.drawable::class.java.fields.filter {
            it.name.startsWith(Constants.PACK)
        }.map {
            val resourceId = it.getInt(null)
            resourceId to ResourcesCompat.getDrawable(resources, resourceId, null)
        }
    }
}