package io.github.yamin8000.cafe.db.entities.category

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Category(
    var name: String,
    var imageId: Int = 0,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) : Parcelable {
    //this method is overridden for using this class in auto complete view
    override fun toString() = this.name
}
