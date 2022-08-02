package com.app.msm.ui.main.monitoring

import android.graphics.Color
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.msm.databinding.ItemMonitoringBinding
import com.app.msm.databinding.ItemMonitoringGaugeBinding
import com.app.msm.extension.inflateBinding
import com.app.msm.extension.isUsingNightMode
import com.app.msm.extension.orZero
import com.app.msm.helper.HexColor
import com.app.msm.model.monitoring.Monitor
import com.ekn.gruzer.gaugelibrary.Range

class MonitoringAdapter : ListAdapter<Monitor, RecyclerView.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder = when (viewType) {
        Monitor.VIEW_TYPE_GAUGE -> GaugeViewHolder(
            parent.inflateBinding(ItemMonitoringGaugeBinding::inflate)
        )
        else -> MonitorViewHolder(
            parent.inflateBinding(ItemMonitoringBinding::inflate)
        )
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when (holder) {
            is GaugeViewHolder -> getItem(position)?.let(holder::bind)
            is MonitorViewHolder -> getItem(position)?.let(holder::bind)
        }
    }

    override fun getItemViewType(position: Int): Int = getItem(position).type.view

    private inner class MonitorViewHolder(
        private val binding: ItemMonitoringBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(monitor: Monitor) = with(binding) {
            tvLabel.text = root.context.getString(monitor.label)
            tvValue.text = monitor.value
        }
    }

    private inner class GaugeViewHolder(
        private val binding: ItemMonitoringGaugeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(monitor: Monitor) = with(binding) {
            tvLabel.text = root.context.getString(monitor.label)
            initGauge(monitor)
        }

        private fun initGauge(monitor: Monitor) = with(binding) {
            val (firstRange, secondRange, thirdRange) = when (monitor.type.data) {
                Monitor.DATA_TYPE_TEMP -> Triple(
                    Range().apply {
                        color = Color.parseColor(HexColor.DARK_BLUE)
                        from = 0.0
                        to = 22.0
                    },
                    Range().apply {
                        color = Color.parseColor(HexColor.GREEN)
                        from = 23.0
                        to = 32.0
                    },
                    Range().apply {
                        color = Color.parseColor(HexColor.RED)
                        from = 32.0
                        to = 62.0
                    }
                )
                Monitor.DATA_TYPE_HUMIDITY -> Triple(
                    Range().apply {
                        color = Color.parseColor(HexColor.BROWN)
                        from = 0.0
                        to = 69.0
                    },
                    Range().apply {
                        color = Color.parseColor(HexColor.GREEN)
                        from = 70.0
                        to = 90.0
                    },
                    Range().apply {
                        color = Color.parseColor(HexColor.DARK_BLUE)
                        from = 90.0
                        to = 100.0
                    }
                )
                else -> return@with
            }
            val textColor = if (root.context.isUsingNightMode()) {
                Color.WHITE
            } else {
                Color.BLACK
            }
            gauge.apply {
                addRange(firstRange)
                addRange(secondRange)
                addRange(thirdRange)
                valueColor = textColor
                maxValueTextColor = textColor
                minValueTextColor = textColor
                minValue = firstRange.from
                maxValue = thirdRange.to
                value = monitor.value.toIntOrNull().orZero().toDouble()
            }
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
