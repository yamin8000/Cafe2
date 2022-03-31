package io.github.yamin8000.cafe.subscriber

import io.github.yamin8000.cafe.databinding.SubscriberItemBinding
import io.github.yamin8000.cafe.db.entities.subscriber.Subscriber
import io.github.yamin8000.cafe.ui.recyclerview.AsyncDifferHelper.getAsyncDiffer
import io.github.yamin8000.cafe.ui.crud.CrudAdapter

class SubscriberAdapter(
    private val updateCallback: (Subscriber) -> Unit,
    private val deleteCallback: (Subscriber, Boolean) -> Unit
) : CrudAdapter<Subscriber, SubscriberHolder>() {

    override var asyncList = this.getAsyncDiffer<Subscriber, SubscriberHolder>(
        { old, new -> old.id == new.id },
        { old, new -> old == new }
    )

    init {
        initAdapter({ parent, inflater ->
            val binding = SubscriberItemBinding.inflate(inflater, parent, false)
            SubscriberHolder(asyncList, binding, updateCallback, deleteCallback)
        }, { holder, position ->
            holder.bind(asyncList.currentList[position])
        }, { asyncList.currentList.size })
    }
}