package io.github.yamin8000.cafe.ui

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import io.github.yamin8000.cafe.R

class AutoArrayAdapter<T>(
    private val context: Context,
    private val items: List<T>,
    private val autoCompleteTextView: AutoCompleteTextView,
    private val onItemSelectListener: (T, Int) -> Unit
) {
    init {
        if (items.isEmpty()) autoCompleteTextView.isEnabled = false
        else setAdapter()
    }

    private fun setAdapter() {
        ArrayAdapter(context, R.layout.dropdown_item, items).let { adapter ->
            autoCompleteTextView.setAdapter(adapter)
            autoCompleteTextView.setOnItemClickListener { adapterView, _, position, _ ->
                val item = adapterView.getItemAtPosition(position) as T
                onItemSelectListener(item, position)
            }
        }
    }
}