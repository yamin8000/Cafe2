package io.github.yamin8000.cafe.worker.schedule

import android.widget.ArrayAdapter
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentNewScheduleBinding
import io.github.yamin8000.cafe.db.entities.relatives.ScheduleAndWorker
import io.github.yamin8000.cafe.db.entities.worker.Worker
import io.github.yamin8000.cafe.db.entities.worker.schedule.Schedule
import io.github.yamin8000.cafe.ui.crud.CreateUpdateFragment
import io.github.yamin8000.cafe.util.Constants.NO_ID_LONG
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.DateTimeUtils
import io.github.yamin8000.cafe.util.DateTimeUtils.toJalali
import io.github.yamin8000.cafe.util.DateTimeUtils.toJalaliIso
import io.github.yamin8000.cafe.util.Utility.Alerts.snack
import io.github.yamin8000.cafe.util.Utility.hideKeyboard
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewScheduleFragment :
    CreateUpdateFragment<ScheduleAndWorker, FragmentNewScheduleBinding>(
        ScheduleAndWorker(Schedule(), Worker()),
        { FragmentNewScheduleBinding.inflate(it) }
    ) {

    override fun init() {
        lifecycleScope.launch { handleWorkersAutoComplete() }
        timePickersHandler()
    }

    private fun timePickersHandler() {
        binding.scheduleStartButton.setOnClickListener {
            datePicker(getString(R.string.schedule_start)) { hour, minute ->
                item.schedule.start = DateTimeUtils.zonedNow()
                    .withHour(hour)
                    .withMinute(minute)
                    .withSecond(0)
                    .withNano(0)
                binding.scheduleStartButton.text = item.schedule.start?.toJalaliIso()
            }
        }
        binding.scheduleEndButton.setOnClickListener {
            datePicker(getString(R.string.schedule_end)) { hour, minute ->
                item.schedule.end = DateTimeUtils.zonedNow()
                    .withHour(hour)
                    .withMinute(minute)
                    .withSecond(0)
                    .withNano(0)
                binding.scheduleEndButton.text = item.schedule.end?.toJalaliIso()
            }
        }
    }

    private suspend fun handleWorkersAutoComplete() {
        val workers = withContext(ioScope.coroutineContext) {
            db?.workerDao()?.getAll() ?: listOf()
        }
        if (workers.isNotEmpty())
            fillWorkersAutoComplete(workers)
        else handleEmptyWorkers()
    }

    private fun fillWorkersAutoComplete(workers: List<Worker>) {
        binding.scheduleWorkerInput.isEnabled = true
        context?.let {
            ArrayAdapter(it, R.layout.dropdown_item, workers).let { adapter ->
                binding.scheduleWorkerAuto.setAdapter(adapter)
                binding.scheduleWorkerAuto.setOnItemClickListener { _, _, position, _ ->
                    val worker = adapter.getItem(position) as Worker
                    item.schedule.workerId = worker.id
                }
            }
        }
    }

    private fun handleEmptyWorkers() {
        binding.scheduleWorkerInput.isEnabled = false
        binding.scheduleConfirmButton.isEnabled = false
        snack(getString(R.string.no_workers_to_schedule))
    }

    override fun initViewForCreateMode() {
        //
    }

    private fun datePicker(title: String, time: (Int, Int) -> Unit) {
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setTitleText(title)
            .build()
        picker.show(childFragmentManager, null)
        picker.addOnPositiveButtonClickListener { time(picker.hour, picker.minute) }
    }

    override fun initViewForEditMode() {
        binding.scheduleConfirmButton.text = getString(R.string.edit)
        binding.scheduleWorkerAuto.setText(item.worker.toString())
        item.schedule.description?.let { binding.scheduleDescriptionEdit.setText(it) }
        item.schedule.start?.let { binding.scheduleStartButton.text = it.toJalaliIso() }
        item.schedule.end?.let { binding.scheduleEndButton.text = it.toJalaliIso() }
        binding.scheduleDayText.text = item.schedule.day.toJalali()
    }

    override suspend fun createItem() {
        withContext(ioScope.coroutineContext) {
            db?.scheduleDao()?.insert(item.schedule)
        }.let { id ->
            if (id != null) scheduleAddSuccess()
            else snack(getString(R.string.db_update_error))
        }
    }

    private fun scheduleAddSuccess() {
        snack(getString(R.string.item_add_success, getString(R.string.schedule)))
        clearViews()
        clearValues()
        hideKeyboard()
    }

    private fun clearValues() {
        item = ScheduleAndWorker(Schedule(), Worker())
    }

    private fun clearViews() {
        binding.scheduleStartButton.text = getString(R.string.schedule_start)
        binding.scheduleEndButton.text = getString(R.string.schedule_end)
        binding.scheduleDescriptionEdit.text?.clear()
        binding.scheduleWorkerAuto.text.clear()
    }

    override suspend fun editItem() {
        if (item.schedule.id != NO_ID_LONG) {
            withContext(ioScope.coroutineContext) {
                db?.scheduleDao()?.update(item.schedule)
            }.let { id ->
                if (id != null) {
                    snack(
                        getString(
                            R.string.item_edit_success,
                            getString(R.string.schedule)
                        )
                    )
                } else snack(getString(R.string.db_update_error))
            }
        }
    }

    override fun validator(): Boolean {
        return item.schedule.workerId != NO_ID_LONG &&
                item.schedule.start != null
    }

    override fun confirm() {
        binding.scheduleConfirmButton.setOnClickListener {
            binding.scheduleDescriptionEdit.text?.toString().let {
                if (!it.isNullOrBlank())
                    item.schedule.description = it.toString()
            }
            confirmListener(this::validator)
        }
    }
}