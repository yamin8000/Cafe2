package io.github.yamin8000.cafe.db.entities.worker.payment

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.ZonedDateTime

@Parcelize
@Entity
data class Payment(
    var value: Long,
    var workerId: Long,
    var date: ZonedDateTime = ZonedDateTime.now(),
    var description: String? = null,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
) : Parcelable {
    constructor() : this(0, 0)
}