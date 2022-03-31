package io.github.yamin8000.cafe.db.entities.account

import android.content.Context
import io.github.yamin8000.cafe.R

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
            Guest -> context.getString(R.string.guest)
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