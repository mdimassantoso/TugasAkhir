package com.app.msm.ui.main.setting.change_setting

import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.msm.R
import com.app.msm.databinding.FragmentChangeSettingBinding
import com.app.msm.extension.orZero
import com.app.msm.extension.showSnackBar
import com.app.msm.helper.Helper.showTimePickerDialog
import com.app.msm.helper.viewBinding
import com.app.msm.model.configuration.Configuration
import com.app.msm.vo.ViewState
import kotlinx.coroutines.launch

class ChangeSettingFragment : Fragment(R.layout.fragment_change_setting) {

    private val binding by viewBinding(FragmentChangeSettingBinding::bind)

    private val viewModel: ChangeSettingViewModel by viewModels()

    private val args: ChangeSettingFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        bindData(args.configuration)
        initObserveResult()
        initListener()
    }

    private fun initView() = with(binding) {
        edtWateringOne.inputType = InputType.TYPE_NULL
        edtWateringTwo.inputType = InputType.TYPE_NULL
        edtWateringThree.inputType = InputType.TYPE_NULL
    }

    private fun initObserveResult() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.configurationViewState.collect { viewState ->
                    handleConfigurationViewState(viewState)
                }
            }
        }
    }

    private fun handleConfigurationViewState(actionState: ViewState<Configuration>) = with(binding) {
        btnSave.isEnabled = actionState !is ViewState.Loading
        when (actionState) {
            is ViewState.Loading -> Unit
            is ViewState.Error -> {
                root.showSnackBar(actionState.e.message.orEmpty())
            }
            is ViewState.Success -> {
                root.showSnackBar("Berhasil mengubah konfigurasi")
                findNavController().navigateUp()
            }
        }
    }

    private fun bindData(configuration: Configuration) = with(binding) {
        configuration.apply {
            edtWateringOne.setText(firstWateringSchedule)
            edtWateringTwo.setText(secondWateringSchedule)
            edtWateringThree.setText(thirdWateringSchedule)
            edtWateringDuration.setText(wateringDuration.toString())
            edtTempUpperLimit.setText(temperatureUpperLimit.toString())
            edtTempLowerLimit.setText(temperatureLowerLimit.toString())
            edtHumUpperLimit.setText(humidityUpperLimit.toString())
            edtHumLowerLimit.setText(humidityLowerLimit.toString())
        }
    }

    private fun initListener() = with(binding) {
        btnSave.setOnClickListener {
            saveConfiguration()
        }
        edtWateringOne.setOnClickListener {
            showTimePickerDialog(childFragmentManager) { time ->
                edtWateringOne.setText(time)
            }
        }
        edtWateringTwo.setOnClickListener {
            showTimePickerDialog(childFragmentManager) { time ->
                edtWateringTwo.setText(time)
            }
        }
        edtWateringThree.setOnClickListener {
            showTimePickerDialog(childFragmentManager) { time ->
                edtWateringThree.setText(time)
            }
        }
    }

    private fun saveConfiguration() = with(binding) {
        val firstWateringSchedule = edtWateringOne.text.toString()
        val secondWateringSchedule = edtWateringTwo.text.toString()
        val thirdWateringSchedule = edtWateringThree.text.toString()
        val wateringDuration = edtWateringDuration.text.toString().toIntOrNull().orZero()
        val tempUpperLimit = edtTempUpperLimit.text.toString().toIntOrNull().orZero()
        val tempLowerLimit = edtTempLowerLimit.text.toString().toIntOrNull().orZero()
        val humUpperLimit = edtHumUpperLimit.text.toString().toIntOrNull().orZero()
        val humLowerLimit = edtHumLowerLimit.text.toString().toIntOrNull().orZero()

        if (firstWateringSchedule.isEmpty() or secondWateringSchedule.isEmpty() or thirdWateringSchedule.isEmpty()
            or (wateringDuration == 0)
            or (tempUpperLimit == 0)
            or (tempLowerLimit == 0)
            or (humUpperLimit == 0)
            or (humLowerLimit == 0)
        ) {
            root.showSnackBar("Konfigurasi harus diisi semua")
            return@with
        }

        Configuration(
            firstWateringSchedule = firstWateringSchedule,
            secondWateringSchedule = secondWateringSchedule,
            thirdWateringSchedule = thirdWateringSchedule,
            wateringDuration = wateringDuration,
            humidityUpperLimit = humUpperLimit,
            humidityLowerLimit = humLowerLimit,
            temperatureUpperLimit = tempUpperLimit,
            temperatureLowerLimit = tempLowerLimit
        ).run {
            viewModel.updateConfiguration(this)
        }
    }
}
