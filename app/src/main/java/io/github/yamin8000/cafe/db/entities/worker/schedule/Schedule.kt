package io.github.yamin8000.cafe.db.entities.worker.schedule

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

@Parcelize
@Entity
data class Schedule(
    var workerId: Long,
    var start: ZonedDateTime? = null,
    var end: ZonedDateTime? = null,
    val day: LocalDate = LocalDate.now(ZoneId.systemDefault()),
    var description: String? = null,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
) : Parcelable {
    constructor() : this(0)
}
