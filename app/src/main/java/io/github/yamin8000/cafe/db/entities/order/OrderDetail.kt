package io.github.yamin8000.cafe.db.entities.order

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import io.github.yamin8000.cafe.db.entities.product.Product
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Order::class,
            parentColumns = ["id"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = Product::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class OrderDetail(
    @ColumnInfo(name = "productId", index = true)
    val productId: Long,
    var quantity: Int,
    var summary: String,
    @ColumnInfo(name = "orderId", index = true)
    var orderId: Long? = null,
    val description: String? = null,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
) : Parcelable