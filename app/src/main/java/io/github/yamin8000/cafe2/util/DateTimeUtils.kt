/*
 *     Cafe/Cafe.app.main
 *     DateTimeUtils.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     DateTimeUtils.kt Last modified at 2022/12/9
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

import org.bardframework.time.LocalDateJalali
import org.bardframework.time.LocalDateTimeJalali
import org.bardframework.time.ZonedDateTimeJalali
import java.time.*
import java.time.format.DateTimeFormatter

@Suppress("unused", "MemberVisibilityCanBePrivate")
object DateTimeUtils {

    fun now(): String = LocalDateTime.now().toIso()

    fun zonedNow(): ZonedDateTime = ZonedDateTime.now()

    fun String.toDateTime(): LocalDateTime = LocalDateTime.parse(this)

    fun LocalDateTime.toIso(): String = DateTimeFormatter.ISO_DATE_TIME.format(this)

    fun LocalDateTime.toJalaliIso(): String {
        return LocalDateTimeJalali.of(this).toString()
    }

    fun ZonedDateTime.toJalaliIso(): String {
        var temp = this.toOriginalJalaliIso()
        temp = temp.replace("T", "\n")
        temp = temp.split('+')[0]
        return temp
    }

    fun ZonedDateTime.toOriginalJalaliIso(): String {
        return ZonedDateTimeJalali.of(
            LocalDateTimeJalali.of(this.toLocalDateTime()),
            ZoneId.systemDefault()
        ).toString()
    }

    fun Long.toIso() = this.toDateTime().toIso()

    fun Long.toDateTime(): LocalDateTime {
        return LocalDateTime.ofEpochSecond(this, 0, ZoneOffset.UTC)
    }

    fun Long.toZonedDateTime(): ZonedDateTime {
        return ZonedDateTime.of(this.toDateTime(), ZoneId.systemDefault())
    }

    fun Long.zonedDateTimeOfMillis(): ZonedDateTime {
        return this.toLocalDateTime().toZonedDateTime()
    }

    fun LocalDateTime.toZonedDateTime(): ZonedDateTime {
        return ZonedDateTime.of(this, ZoneId.systemDefault())
    }

    fun Long.toLocalDateTime(): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
    }

    fun LocalDateTime.ignoreTime(): LocalDateTime {
        return this.withHour(0).withMinute(0).withSecond(0).withNano(0)
    }

    fun LocalDate.toJalali() = LocalDateJalali.of(this).toString()
}