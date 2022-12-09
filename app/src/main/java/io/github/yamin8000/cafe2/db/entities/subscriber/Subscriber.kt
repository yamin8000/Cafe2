/*
 *     Cafe/Cafe.app.main
 *     Subscriber.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     Subscriber.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe2.db.entities.subscriber

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.yamin8000.cafe2.db.entities.NotCreated
import io.github.yamin8000.cafe2.db.entities.NotCreatedDelegate
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Subscriber(
    var name: String,
    var phone: String,
    var address: String,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
) : Parcelable, NotCreated by NotCreatedDelegate(id) {
    constructor() : this("", "", "")

    //this method is overridden for using this class in auto complete view
    override fun toString() = buildString {
        append("#")
        append(id)
        append(" ")
        append(name)
        append(" ")
        append(phone)
    }
}