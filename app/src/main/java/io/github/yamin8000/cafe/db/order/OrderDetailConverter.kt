package io.github.yamin8000.cafe.db.order

import androidx.room.TypeConverter

class OrderDetailConverter {

    @TypeConverter
    fun listToString(list: List<Int>) = list.joinToString(",")

    @TypeConverter
    fun stringToList(value: String) = value.splitToSequence(",").map { it.toInt() }.toList()
}