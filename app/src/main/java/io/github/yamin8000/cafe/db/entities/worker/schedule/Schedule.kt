package io.github.yamin8000.cafe.db.entities.worker.schedule

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.ZonedDateTime

@Parcelize
@Entity
data class Schedule(
    var start: ZonedDateTime,
    var end: ZonedDateTime,
    var workerId: Long,
    var description: String? = null,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
) : Parcelable {
    constructor() : this(ZonedDateTime.now(), ZonedDateTime.now(), 0)
}
