package io.github.yamin8000.cafe.subscriber

import androidx.recyclerview.widget.AsyncListDiffer
import io.github.yamin8000.cafe.databinding.SubscriberItemBinding
import io.github.yamin8000.cafe.db.entities.subscriber.Subscriber
import io.github.yamin8000.cafe.ui.crud.CrudHolder

class SubscriberHolder(
    asyncList: AsyncListDiffer<Subscriber>,
    binding: SubscriberItemBinding,
    updateCallback: (Subscriber) -> Unit,
    deleteCallback: (Subscriber, Boolean) -> Unit
) : CrudHolder<Subscriber, SubscriberItemBinding>(
    asyncList,
    binding,
    updateCallback,
    deleteCallback,
    { subscriber ->
        binding.subscriberId.text = subscriber.id.toString()
        binding.subscriberName.text = subscriber.name
        binding.subscriberAddress.text = subscriber.address
        binding.subscriberPhone.text = subscriber.phone
    }
)