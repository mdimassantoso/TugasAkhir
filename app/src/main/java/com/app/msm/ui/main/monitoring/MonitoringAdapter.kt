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
import kotlin.math.roundToInt

class MonitoringAdapter : ListAdapter<Monitor, RecyclerView.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder = when (viewType) {
        Monitor.ViewType.Gauge.VIEW_TYPE -> GaugeViewHolder(
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
        val item = getItem(position) ?: return
        when (item.viewType) {
            is Monitor.ViewType.Default -> {
                (holder as MonitorViewHolder).bind(item)
            }
            is Monitor.ViewType.DefaultSeparatedUnitMeasurement -> {
                (holder as MonitorViewHolder).bindSeparatedMeasurement(item)
            }
            is Monitor.ViewType.Gauge -> {
                (holder as GaugeViewHolder).bind(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int = getItem(position)?.let { item ->
        when (item.viewType) {
            is Monitor.ViewType.Default -> Monitor.ViewType.Default.VIEW_TYPE
            is Monitor.ViewType.DefaultSeparatedUnitMeasurement -> Monitor.ViewType.DefaultSeparatedUnitMeasurement.VIEW_TYPE
            is Monitor.ViewType.Gauge -> Monitor.ViewType.Gauge.VIEW_TYPE
        }
    } ?: Monitor.ViewType.Default.VIEW_TYPE

    private inner class MonitorViewHolder(
        private val binding: ItemMonitoringBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(monitor: Monitor) = with(binding) {
            tvLabel.text = root.context.getString(monitor.label)
            val value = StringBuilder().apply {
                append(monitor.value)
                append(getUnitMeasurement(monitor.unitMeasurement))
            }.toString()
            tvValue.text = value
        }

        fun bindSeparatedMeasurement(monitor: Monitor) = with(binding) {
            tvLabel.text = root.context.getString(monitor.label)
            tvValue.text = monitor.value
            tvUnitMeasurement.text = getUnitMeasurement(monitor.unitMeasurement)
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
            val (firstRange, secondRange, thirdRange) = when (monitor.dataType) {
                is Monitor.DataType.Temperature -> Triple(
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
                is Monitor.DataType.Humidity -> Triple(
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
            val unitValueMeasurement = getUnitMeasurement(monitor.unitMeasurement)
            gauge.apply {
                setFormatter { value -> "${value.roundToInt()}$unitValueMeasurement" }
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

    private fun getUnitMeasurement(
        measurement: Monitor.UnitMeasurement
    ): String = when (measurement) {
        is Monitor.UnitMeasurement.Celcius -> "C"
        is Monitor.UnitMeasurement.Percent -> "%"
        is Monitor.UnitMeasurement.Day -> "Hari"
        is Monitor.UnitMeasurement.NoMeasurement -> null
    }.orEmpty()

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
