package io.github.yamin8000.cafe.subscriber

import androidx.recyclerview.widget.GridLayoutManager
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.db.entities.subscriber.Subscriber
import io.github.yamin8000.cafe.ui.crud.ReadDeleteFragment
import io.github.yamin8000.cafe.util.Constants.db

class SubscriberFragment : ReadDeleteFragment<Subscriber, SubscriberHolder>(R.id.newSubscriberFragment) {

    override suspend fun getItems(): List<Subscriber> {
        return db.subscriberDao().getAll()
    }

    override suspend fun dbDeleteAction() {
        db.subscriberDao().deleteAll(deleteCandidates)
    }

    override fun fillList() {
        val adapter = SubscriberAdapter(this::updateCallback, this::deleteCallback)
        binding.crudList.adapter = adapter
        binding.crudList.layoutManager = GridLayoutManager(context, 2)
        adapter.asyncList.submitList(items)
    }
}