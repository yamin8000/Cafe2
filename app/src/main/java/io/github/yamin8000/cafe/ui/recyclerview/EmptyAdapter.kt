package io.github.yamin8000.cafe.ui.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.yamin8000.cafe.R

class EmptyAdapter(
    private val message: String = ""
) : RecyclerView.Adapter<EmptyAdapter.EmptyViewHolder>() {

    class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmptyViewHolder {
        val view = inflateView(parent)
        if (message.isNotBlank())
            setMessage(view.findViewById(R.id.empty_adapter_text))
        return EmptyViewHolder(view)
    }

    private fun inflateView(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.empty_adapter, parent, false)
    }

    private fun setMessage(textView: TextView) {
        textView.text = message
    }

    override fun onBindViewHolder(holder: EmptyViewHolder, position: Int) {
        /* no-op */
    }

    override fun getItemCount() = 1
}