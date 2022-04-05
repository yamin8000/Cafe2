package io.github.yamin8000.cafe.db.entities.relatives

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import io.github.yamin8000.cafe.db.entities.worker.Worker
import io.github.yamin8000.cafe.db.entities.worker.schedule.Schedule
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScheduleAndWorker(
    @Embedded
    val schedule: Schedule,
    @Relation(
        parentColumn = "workerId",
        entityColumn = "id"
    )
    val worker: Worker
) : Parcelable