package io.github.yamin8000.cafe.report

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.yamin8000.cafe.databinding.ReportItemBinding

class ReportAdapter(
    private val reports: List<String>,
    private val clickListener: (String) -> Unit
) : RecyclerView.Adapter<ReportAdapter.ReportHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportHolder {
        val binding = ReportItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReportHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: ReportHolder, position: Int) {
        holder.bind(reports[position])
    }

    override fun getItemCount() = reports.size

    class ReportHolder(
        private val binding: ReportItemBinding,
        private val clickListener: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.reportText.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    clickListener(binding.reportText.text.toString())
            }
        }

        fun bind(report: String) {
            binding.reportText.text = report
        }
    }
}