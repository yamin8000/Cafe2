package io.github.yamin8000.cafe.db.entities.product

import androidx.room.Dao
import io.github.yamin8000.cafe.db.entities.BaseDao

@Dao
abstract class ProductDao : BaseDao<Product>("product")