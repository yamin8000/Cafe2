package io.github.yamin8000.cafe.db.entities.category

import androidx.room.Dao
import io.github.yamin8000.cafe.db.entities.BaseDao

@Dao
abstract class CategoryDao : BaseDao<Category>("category")