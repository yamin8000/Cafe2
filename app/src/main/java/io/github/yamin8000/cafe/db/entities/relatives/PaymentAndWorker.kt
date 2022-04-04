package io.github.yamin8000.cafe.db.entities.relatives

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import io.github.yamin8000.cafe.db.entities.worker.Worker
import io.github.yamin8000.cafe.db.entities.worker.payment.Payment
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentAndWorker(
    @Embedded
    val payment: Payment,
    @Relation(
        parentColumn = "workerId",
        entityColumn = "id"
    )
    val worker: Worker,
) : Parcelable