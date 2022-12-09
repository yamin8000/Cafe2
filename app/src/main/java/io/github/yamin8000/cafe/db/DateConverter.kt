/*
 *     Cafe/Cafe.app.main
 *     DateConverter.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     DateConverter.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe.db

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

class DateConverter {

    @TypeConverter
    fun epochToDatetime(epoch: Long): ZonedDateTime {
        val instant = getInstant(epoch)
        return ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
    }

    @TypeConverter
    fun datetimeToEpoch(dateTime: ZonedDateTime): Long = dateTime.toEpochSecond()

    private fun getInstant(epoch: Long) = Instant.ofEpochSecond(epoch)

    @TypeConverter
    fun epochToDate(epoch: Long): LocalDate = LocalDate.ofEpochDay(epoch)

    @TypeConverter
    fun dateToEpoch(date: LocalDate): Long = date.toEpochDay()
}