/*
 *     Cafe/Cafe.app.main
 *     SubscriberDao.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     SubscriberDao.kt Last modified at 2022/12/9
 *     This file is part of Cafe/Cafe.app.main.
 *     Copyright (C) 2022  Yamin Siahmargooei
 *
 *     Cafe/Cafe.app.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cafe/Cafe.app.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cafe.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.cafe2.db.entities.subscriber

import androidx.room.Dao
import io.github.yamin8000.cafe2.db.entities.BaseDao

@Dao
abstract class SubscriberDao : BaseDao<Subscriber>("subscriber")