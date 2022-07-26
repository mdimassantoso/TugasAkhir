package com.app.msm.ui.main.controlling

import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.msm.databinding.ItemControllingBinding
import com.app.msm.extension.inflateBinding
import com.app.msm.model.Control

class ControllingAdapter(
    private val onSwitchChecked: (Control) -> Unit,
    private val onButtonClicked: (Control) -> Unit
) : ListAdapter<Control, RecyclerView.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder = ControlViewHolder(
        parent.inflateBinding(ItemControllingBinding::inflate)
    )

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val viewHolder = holder as? ControlViewHolder ?: return
        getItem(position)?.let(viewHolder::bind)
    }

    private inner class ControlViewHolder(
        private val binding: ItemControllingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(control: Control) = with(binding) {
            tvLabel.text = root.context.getString(control.label)
            ivIcon.setImageResource(control.icon)
            val controlType = control.type
            swControl.isInvisible = controlType is ControlType.Button

            when (controlType) {
                is ControlType.Switch -> {
                    swControl.isChecked = controlType.isChecked
                    swControl.setOnCheckedChangeListener { _, isChecked ->
                        onSwitchChecked.invoke(
                            control.copy(type = ControlType.Switch(isChecked))
                        )
                    }
                }
                is ControlType.Button -> {
                    conContent.setOnClickListener { onButtonClicked.invoke(control) }
                }
            }
        }
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<Control>() {
            override fun areItemsTheSame(
                oldItem: Control,
                newItem: Control
            ): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(
                oldItem: Control,
                newItem: Control
            ): Boolean = oldItem == newItem
        }
    }
}
