package io.github.yamin8000.cafe.db.entities.worker

import androidx.room.Dao
import io.github.yamin8000.cafe.db.entities.BaseDao

@Dao
abstract class WorkerDao : BaseDao<Worker>("worker")