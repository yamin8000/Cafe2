package io.github.yamin8000.cafe.db.entities.order

import androidx.room.TypeConverter

class OrderDetailConverter {

    @TypeConverter
    fun listToString(list: List<Long>) = list.joinToString(",")

    @TypeConverter
    fun stringToList(value: String) = value.splitToSequence(",").map { it.toLong() }.toList()
}