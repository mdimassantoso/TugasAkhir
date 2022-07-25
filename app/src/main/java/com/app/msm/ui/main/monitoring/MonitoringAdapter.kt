package com.app.msm.ui.main.monitoring

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.msm.databinding.ItemMonitoringBinding
import com.app.msm.extension.inflateBinding
import com.app.msm.model.Monitor

class MonitoringAdapter : ListAdapter<Monitor, RecyclerView.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder = MonitorViewHolder(
        parent.inflateBinding(ItemMonitoringBinding::inflate)
    )

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val viewHolder = holder as? MonitorViewHolder ?: return
        getItem(position)?.let(viewHolder::bind)
    }

    private inner class MonitorViewHolder(
        private val binding: ItemMonitoringBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(monitor: Monitor) = with(binding) {
            tvLabel.text = root.context.getString(monitor.label)
            tvValue.text = monitor.value
        }
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<Monitor>() {
            override fun areItemsTheSame(
                oldItem: Monitor,
                newItem: Monitor
            ): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(
                oldItem: Monitor,
                newItem: Monitor
            ): Boolean = oldItem == newItem
        }
    }
}
