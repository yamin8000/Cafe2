package io.github.yamin8000.cafe.db.entities.day

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class Day(
    val date: LocalDate,
    var lastOrderId: Int = 1,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)