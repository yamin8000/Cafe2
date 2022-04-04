package io.github.yamin8000.cafe.db.entities.worker.schedule

import androidx.room.Dao
import io.github.yamin8000.cafe.db.entities.BaseDao

@Dao
abstract class ScheduleDao : BaseDao<Schedule>("schedule")