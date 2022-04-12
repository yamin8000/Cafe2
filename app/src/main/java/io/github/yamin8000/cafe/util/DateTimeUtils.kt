package io.github.yamin8000.cafe.util

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