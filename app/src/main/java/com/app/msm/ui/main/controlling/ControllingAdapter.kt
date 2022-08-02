package com.app.msm.ui.main.controlling

import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.msm.databinding.ItemControllingBinding
import com.app.msm.extension.inflateBinding
import com.app.msm.model.controlling.Control

class ControllingAdapter(
    private val onSwitchChecked: (id: Int, isChecked: Boolean) -> Unit,
    private val onButtonClicked: (id: Int) -> Unit,
    private val onAutoConfigEnabled: () -> Unit,
    var isDisableAction: Boolean = false,
    var isAutoConfigEnable: Boolean = false
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
                    swControl.setOnCheckedChangeListener { switch, isChecked ->
                        handleOnCheckedListener(control, switch, isChecked)
                    }
                }
                is ControlType.Button -> {
                    conContent.setOnClickListener {
                        handleOnClickListener(control)
                    }
                }
            }
        }
    }

    private fun handleOnClickListener(control: Control) {
        if (!isDisableAction) {
            onButtonClicked.invoke(control.id)
        }
    }

    private fun handleOnCheckedListener(
        control: Control,
        switch: CompoundButton,
        isChecked: Boolean
    ) {
        if (!isDisableAction && !isAutoConfigEnable) {
            onSwitchChecked.invoke(control.id, isChecked)
            return
        }
        switch.isChecked = isChecked.not()
        if (isAutoConfigEnable) {
            onAutoConfigEnabled.invoke()
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
