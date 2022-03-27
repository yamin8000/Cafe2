package io.github.yamin8000.cafe.db.entities.subscriber

import androidx.room.Dao
import io.github.yamin8000.cafe.db.entities.BaseDao

@Dao
abstract class SubscriberDao : BaseDao<Subscriber>("subscriber")