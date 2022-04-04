package io.github.yamin8000.cafe.subscriber

import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentNewSubscriberBinding
import io.github.yamin8000.cafe.db.entities.subscriber.Subscriber
import io.github.yamin8000.cafe.ui.crud.CreateUpdateFragment
import io.github.yamin8000.cafe.util.Constants.NO_ID_LONG
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.Alerts.snack
import io.github.yamin8000.cafe.util.Utility.hideKeyboard
import kotlinx.coroutines.withContext

class NewSubscriberFragment :
    CreateUpdateFragment<Subscriber, FragmentNewSubscriberBinding>(
        Subscriber(),
        { FragmentNewSubscriberBinding.inflate(it) }
    ) {

    override fun init() {
        //
    }

    override fun initViewForCreateMode() {
        //
    }

    override fun initViewForEditMode() {
        binding.newSubscriberConfirm.text = getString(R.string.edit)
        if (item.id != NO_ID_LONG) {
            binding.subscriberNameEdit.setText(item.name)
            binding.subscriberAddressEdit.setText(item.address)
            binding.subscriberPhoneEdit.setText(item.phone)
        }
    }

    override suspend fun createItem() {
        val id = db?.subscriberDao()?.insert(item)
        if (id != null) addSuccess()
    }

    override suspend fun editItem() {
        if (item.id != NO_ID_LONG) {
            withContext(ioScope.coroutineContext) {
                db?.subscriberDao()?.update(item)
            }
            editSuccess()
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
            confirmListener(this::validator)
        }
    }

    private fun addSuccess() {
        snack(getString(R.string.item_add_success, getString(R.string.subscriber)))
        clear()
    }

    private fun clear() {
        hideKeyboard()
        binding.subscriberAddressEdit.text?.clear()
        binding.subscriberNameEdit.text?.clear()
        binding.subscriberPhoneEdit.text?.clear()
        item = Subscriber()
    }

    private fun editSuccess() {
        snack(getString(R.string.item_edit_success, getString(R.string.subscriber)))
        hideKeyboard()
    }
}
