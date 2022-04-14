package io.github.yamin8000.cafe.db.entities.account

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.yamin8000.cafe.db.entities.NotCreated
import io.github.yamin8000.cafe.db.entities.NotCreatedDelegate
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Account(
    var username: String,
    var password: String,
    var permission: AccountPermission = AccountPermission.Guest,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
) : Parcelable, NotCreated by NotCreatedDelegate(id) {
    constructor() : this("", "")
}
