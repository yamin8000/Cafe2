package io.github.yamin8000.cafe.worker.payment

import androidx.recyclerview.widget.LinearLayoutManager
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.db.entities.relatives.PaymentAndWorker
import io.github.yamin8000.cafe.ui.crud.ReadDeleteFragment
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.Alerts.snack
import kotlinx.coroutines.withContext

class PaymentFragment :
    ReadDeleteFragment<PaymentAndWorker, PaymentHolder>(R.id.newPaymentFragment) {

    override suspend fun getItems(): List<PaymentAndWorker> {
        return withContext(ioScope.coroutineContext) {
            db?.relativeDao()?.getPaymentsAndWorkers()
        } ?: listOf()
    }

    override suspend fun dbDeleteAction() {
        withContext(ioScope.coroutineContext) {
            db?.paymentDao()?.deleteAll(deleteCandidates.map { it.payment })
        }.let { id ->
            if (id != null) {
                snack(
                    getString(
                        R.string.item_delete_success,
                        getString(R.string.payment)
                    )
                )
            } else snack(getString(R.string.db_update_error))
        }
    }

    override fun fillList() {
        PaymentAdapter(this::updateCallback, this::deleteCallback).apply {
            binding.crudList.adapter = this
            context?.let { binding.crudList.layoutManager = LinearLayoutManager(it) }
            this.asyncList.submitList(items)
        }
    }
}