package io.github.yamin8000.cafe.db.entities.worker

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Worker(
    var name: String,
    var phone: String,
    var job: String,
    var address: String,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
) : Parcelable {
    constructor() : this("", "", "", "")
}
