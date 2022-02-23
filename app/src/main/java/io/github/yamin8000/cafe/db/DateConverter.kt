package io.github.yamin8000.cafe.db

import androidx.room.TypeConverter
import io.github.yamin8000.cafe.util.DateTimeUtils.toDateTime
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

class DateConverter {

    @TypeConverter
    fun epochToDatetime(epoch: Long) = epoch.toDateTime()

    @TypeConverter
    fun datetimeToEpoch(dateTime: LocalDateTime) = dateTime.toEpochSecond(ZoneOffset.UTC)

    @TypeConverter
    fun epochToDate(epoch: Long) = LocalDate.ofEpochDay(epoch)

    @TypeConverter
    fun dateToEpoch(date: LocalDate) = date.toEpochDay()
}