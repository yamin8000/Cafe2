package io.github.yamin8000.cafe.db.entities.account

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Account(
    var username: String,
    var password: String,
    var permission: AccountPermission = AccountPermission.Guest,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
) : Parcelable {
    constructor() : this("", "")
}
