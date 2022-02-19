package io.github.yamin8000.cafe.db

import androidx.room.TypeConverter
import io.github.yamin8000.cafe.util.DateUtils
import java.time.LocalDateTime
import java.time.ZoneOffset

class DateConverter {

    @TypeConverter
    fun epochToDatetime(epoch: Long) = DateUtils.dateTimeOfEpoch(epoch)

    @TypeConverter
    fun datetimeToEpoch(dateTime: LocalDateTime) = dateTime.toEpochSecond(ZoneOffset.UTC)
}