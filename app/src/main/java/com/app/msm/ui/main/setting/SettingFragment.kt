package com.app.msm.ui.main.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.app.msm.R
import com.app.msm.databinding.FragmentSettingBinding
import com.app.msm.extension.orDash
import com.app.msm.extension.showSnackBar
import com.app.msm.helper.viewBinding
import com.app.msm.model.configuration.Configuration
import com.app.msm.ui.auth.login.LoginActivity
import com.app.msm.vo.ViewState
import kotlinx.coroutines.launch

class SettingFragment : Fragment(R.layout.fragment_setting) {

    private val binding by viewBinding(FragmentSettingBinding::bind)

    private val viewModel: SettingViewModel by viewModels()

    private var configuration: Configuration? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initObserveResult()
        viewModel.listenToConfiguration()
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

    private fun handleConfigurationViewState(viewState: ViewState<Configuration>) {
        when (viewState) {
            is ViewState.Loading -> Unit
            is ViewState.Error -> {
                binding.root.showSnackBar(viewState.e.message.orEmpty())
            }
            is ViewState.Success -> {
                configuration = viewState.data
                bindData(viewState.data)
            }
        }
    }

    private fun bindData(configuration: Configuration) = with(binding) {
        configuration.apply {
            edtWateringOne.setText(firstWateringSchedule.orDash())
            edtWateringTwo.setText(secondWateringSchedule.orDash())
            edtWateringThree.setText(thirdWateringSchedule.orDash())
            edtWateringDuration.setText(wateringDuration.toString())
            edtTempUpperLimit.setText(temperatureUpperLimit.toString())
            edtTempLowerLimit.setText(temperatureLowerLimit.toString())
            edtHumUpperLimit.setText(humidityUpperLimit.toString())
            edtHumLowerLimit.setText(humidityLowerLimit.toString())
        }
    }

    private fun initListener() = with(binding) {
        btnChange.setOnClickListener {
            navigateToChangeSetting()
        }

        btnLogout.setOnClickListener {
            viewModel.logout()
            navigateToLogin()
        }
    }

    private fun navigateToChangeSetting() {
        configuration?.run {
            val direction = SettingFragmentDirections.toUpdateSetting(this)
            findNavController().navigate(direction)
        }
    }

    override fun onDestroyView() {
        viewModel.removeConfigurationListener()
        super.onDestroyView()
    }

    private fun navigateToLogin() {
        LoginActivity.start(requireContext(), clearTask = true)
    }
}
