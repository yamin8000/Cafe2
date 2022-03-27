package io.github.yamin8000.cafe.db.entities.subscriber

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Subscriber(
    var name: String,
    var phone: String,
    var address: String,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
) : Parcelable