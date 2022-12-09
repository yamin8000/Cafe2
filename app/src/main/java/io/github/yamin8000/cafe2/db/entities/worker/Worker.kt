/*
 *     Cafe/Cafe.app.main
 *     Worker.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     Worker.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe2.db.entities.worker

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.yamin8000.cafe2.db.entities.NotCreated
import io.github.yamin8000.cafe2.db.entities.NotCreatedDelegate
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Worker(
    var name: String,
    var phone: String,
    var job: String,
    var address: String,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
) : Parcelable, NotCreated by NotCreatedDelegate(id) {
    constructor() : this("", "", "", "")

    override fun toString() = this.name

    fun describe() = buildString {
        append(this@Worker.name)
        append("\n")
        append(this@Worker.job)
        append("\n")
        append(this@Worker.phone)
    }
}
