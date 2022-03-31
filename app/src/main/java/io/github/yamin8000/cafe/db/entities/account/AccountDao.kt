package io.github.yamin8000.cafe.db.entities.account

import androidx.room.Dao
import io.github.yamin8000.cafe.db.entities.BaseDao

@Dao
abstract class AccountDao : BaseDao<Account>("account")