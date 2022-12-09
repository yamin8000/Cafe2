/*
 *     Cafe/Cafe.app.main
 *     PaymentFragment.kt Copyrighted by Yamin Siahmargooei at 2022/12/9
 *     PaymentFragment.kt Last modified at 2022/12/9
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

package io.github.yamin8000.cafe2.worker.payment

import androidx.recyclerview.widget.LinearLayoutManager
import io.github.yamin8000.cafe2.R
import io.github.yamin8000.cafe2.db.entities.relatives.PaymentAndWorker
import io.github.yamin8000.cafe2.ui.crud.ReadDeleteFragment
import io.github.yamin8000.cafe2.util.Constants.db
import io.github.yamin8000.cafe2.util.Utility.Alerts.snack
import kotlinx.coroutines.withContext

class PaymentFragment :
    ReadDeleteFragment<PaymentAndWorker, PaymentHolder>(R.id.newPaymentFragment) {

    override suspend fun getItems(): List<PaymentAndWorker> {
        return withContext(ioScope.coroutineContext) {
            db.relativeDao().getPaymentsAndWorkers()
        }
    }

    override suspend fun dbDeleteAction() {
        withContext(ioScope.coroutineContext) {
            db.paymentDao().deleteAll(deleteCandidates.map { it.payment })
        }
        snack(getString(R.string.item_delete_success, getString(R.string.payment)))
    }

    override fun fillList() {
        PaymentAdapter(this::updateCallback, this::deleteCallback).apply {
            binding.crudList.adapter = this
            context?.let { binding.crudList.layoutManager = LinearLayoutManager(it) }
            this.asyncList.submitList(items)
        }
    }
}