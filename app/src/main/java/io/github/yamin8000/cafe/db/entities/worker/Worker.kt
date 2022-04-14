package io.github.yamin8000.cafe.db.entities.worker

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.yamin8000.cafe.db.entities.NotCreated
import io.github.yamin8000.cafe.db.entities.NotCreatedDelegate
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
) : Parcelable, NotCreated by NotCreatedDelegate(id) {
    constructor() : this("", "", "", "")

    override fun toString() = this.name

    fun describe() = buildString {
        append(this@Worker.name)
        append("\n")
        append(this@Worker.job)
        append("\n")
        append(this@Worker.phone)
    }
}
