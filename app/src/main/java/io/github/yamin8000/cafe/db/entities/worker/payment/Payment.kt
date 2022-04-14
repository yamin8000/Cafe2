package io.github.yamin8000.cafe.db.entities.worker.payment

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.yamin8000.cafe.db.entities.NotCreated
import io.github.yamin8000.cafe.db.entities.NotCreatedDelegate
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
) : Parcelable, NotCreated by NotCreatedDelegate(id) {
    constructor() : this(0, 0)
}