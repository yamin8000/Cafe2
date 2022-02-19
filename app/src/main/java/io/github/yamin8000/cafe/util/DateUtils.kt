package io.github.yamin8000.cafe.util

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object DateUtils {

    fun now(): String = isoOfDateTime(LocalDateTime.now())

    fun dateTimeOfTimestamp(timestamp: String) = LocalDateTime.parse(timestamp)

    fun isoOfDateTime(localDateTime: LocalDateTime): String {
        return DateTimeFormatter.ISO_DATE_TIME.format(localDateTime)
    }

    fun isoOfEpoch(epoch: Long) = isoOfDateTime(dateTimeOfEpoch(epoch))

    fun dateTimeOfEpoch(epoch: Long): LocalDateTime {
        return LocalDateTime.ofEpochSecond(epoch, 0, ZoneOffset.UTC)
    }
}