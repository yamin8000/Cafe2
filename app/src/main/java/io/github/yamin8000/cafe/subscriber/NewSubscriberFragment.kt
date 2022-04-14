package io.github.yamin8000.cafe.subscriber

import android.os.Bundle
import android.view.View
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentNewSubscriberBinding
import io.github.yamin8000.cafe.db.entities.subscriber.Subscriber
import io.github.yamin8000.cafe.ui.crud.CreateUpdateFragment
import io.github.yamin8000.cafe.util.Constants.db
import kotlinx.coroutines.withContext

class NewSubscriberFragment :
    CreateUpdateFragment<Subscriber, FragmentNewSubscriberBinding>(
        Subscriber(),
        { FragmentNewSubscriberBinding.inflate(it) }
    ) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(binding.newSubscriberConfirm)
    }

    override fun initViewForCreateMode() {
        //
    }

    override fun initViewForEditMode() {
        if (item.isCreated()) {
            binding.subscriberNameEdit.setText(item.name)
            binding.subscriberAddressEdit.setText(item.address)
            binding.subscriberPhoneEdit.setText(item.phone)
        }
    }

    override suspend fun createItem() {
        db.subscriberDao().insert(item)
        addSuccess(getString(R.string.subscriber))
    }

    override suspend fun editItem() {
        if (item.isCreated()) {
            withContext(ioScope.coroutineContext) { db.subscriberDao().update(item) }
            editSuccess(getString(R.string.subscriber))
        }
    }

    override fun validator(): Boolean {
        return binding.subscriberNameEdit.text.toString().isNotBlank() &&
                binding.subscriberPhoneEdit.text.toString().isNotBlank()
    }

    override fun confirm() {
        binding.newSubscriberConfirm.setOnClickListener {
            item.name = binding.subscriberNameEdit.text.toString()
            item.address = binding.subscriberAddressEdit.text.toString()
            item.phone = binding.subscriberPhoneEdit.text.toString()
            confirmItem()
        }
    }

    override fun resetViews() {
        binding.subscriberAddressEdit.text?.clear()
        binding.subscriberNameEdit.text?.clear()
        binding.subscriberPhoneEdit.text?.clear()
    }
}
