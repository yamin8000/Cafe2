package io.github.yamin8000.cafe.db.entities.day

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class Day(
    @PrimaryKey
    val date: LocalDate,
    var lastOrderId: Int = 1,
)