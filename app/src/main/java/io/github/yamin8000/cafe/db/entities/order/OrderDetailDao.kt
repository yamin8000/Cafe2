package io.github.yamin8000.cafe.db.entities.order

import androidx.room.Dao
import io.github.yamin8000.cafe.db.entities.BaseDao

@Dao
abstract class OrderDetailDao : BaseDao<OrderDetail>("orderdetail")