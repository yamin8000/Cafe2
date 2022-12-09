/*
 *     Cafe/Cafe.app.main
 *     AccountPermission.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     AccountPermission.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe2.db.entities.account

import android.content.Context
import io.github.yamin8000.cafe2.R

enum class AccountPermission(val rank: Int) {

    Superuser(0),
    ReportUser(1),
    ScheduleUser(2),
    FinanceUser(3),
    OrderUser(4),
    Guest(5);

    fun defineToString(context: Context): String {
        return when (this) {
            Superuser -> context.getString(R.string.superuser)
            ReportUser -> context.getString(R.string.report_user)
            ScheduleUser -> context.getString(R.string.schedule_user)
            FinanceUser -> context.getString(R.string.finance_user)
            OrderUser -> context.getString(R.string.order_user)
            Guest -> context.getString(R.string.guest_user)
        }
    }

    companion object {
        fun getPermission(rank: Int): AccountPermission {
            return when (rank) {
                0 -> Superuser
                1 -> ReportUser
                2 -> ScheduleUser
                3 -> FinanceUser
                5 -> Guest
                else -> Guest
            }
        }
    }
}