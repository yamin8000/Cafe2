/*
 *     Cafe/Cafe.app.main
 *     Order.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     Order.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe2.db.entities.order

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import io.github.yamin8000.cafe2.db.entities.NotCreated
import io.github.yamin8000.cafe2.db.entities.NotCreatedDelegate
import io.github.yamin8000.cafe2.db.entities.subscriber.Subscriber
import java.time.ZonedDateTime

@Entity(
    foreignKeys = [ForeignKey(
        entity = Subscriber::class,
        parentColumns = ["id"],
        childColumns = ["subscriberId"],
        onDelete = ForeignKey.RESTRICT
    )]
)
data class Order(
    val dayId: Int,
    val date: ZonedDateTime,
    var totalPrice: Long = 0,
    @ColumnInfo(name = "subscriberId", index = true)
    val subscriberId: Long? = null,
    val description: String? = null,
    var status: OrderStatus = OrderStatus.Registered,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
) : NotCreated by NotCreatedDelegate(id)