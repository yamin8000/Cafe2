package io.github.yamin8000.cafe.db.entities.subscriber

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.yamin8000.cafe.db.entities.NotCreated
import io.github.yamin8000.cafe.db.entities.NotCreatedDelegate
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Subscriber(
    var name: String,
    var phone: String,
    var address: String,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
) : Parcelable, NotCreated by NotCreatedDelegate(id) {
    constructor() : this("", "", "")

    //this method is overridden for using this class in auto complete view
    override fun toString() = buildString {
        append("#")
        append(id)
        append(" ")
        append(name)
        append(" ")
        append(phone)
    }
}