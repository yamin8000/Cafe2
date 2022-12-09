/*
 *     Cafe/Cafe.app.main
 *     Product.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     Product.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe.db.entities.product

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import io.github.yamin8000.cafe.db.entities.NotCreated
import io.github.yamin8000.cafe.db.entities.NotCreatedDelegate
import io.github.yamin8000.cafe.db.entities.category.Category
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.RESTRICT
    )]
)
data class Product(
    @ColumnInfo(name = "productName")
    var name: String,
    var price: Long,
    @ColumnInfo(name = "categoryId", index = true)
    var categoryId: Long,
    var imageId: Int? = null,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
) : Parcelable, NotCreated by NotCreatedDelegate(id) {
    constructor() : this("", 0, -1, null)
}
