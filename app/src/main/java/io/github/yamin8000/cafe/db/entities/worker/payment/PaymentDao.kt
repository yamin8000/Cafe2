package io.github.yamin8000.cafe.db.entities.worker.payment

import androidx.room.Dao
import io.github.yamin8000.cafe.db.entities.BaseDao

@Dao
abstract class PaymentDao : BaseDao<Payment>("payment")