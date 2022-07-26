package com.app.msm.ui.main.monitoring

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.msm.R
import com.app.msm.databinding.FragmentMonitoringBinding
import com.app.msm.extension.showSnackBar
import com.app.msm.helper.viewBinding
import com.app.msm.model.Control
import com.app.msm.model.Monitor
import com.app.msm.ui.main.controlling.ControlType
import com.app.msm.ui.main.controlling.ControllingViewModel
import com.app.msm.vo.ViewState
import kotlinx.coroutines.launch

class MonitoringFragment : Fragment(R.layout.fragment_monitoring) {

    private val binding by viewBinding(FragmentMonitoringBinding::bind)

    private val viewModel: MonitoringViewModel by viewModels()

    private val monitoringAdapter by lazy { MonitoringAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initObserveResult()
        viewModel.listenToMonitoredData()
    }

    private fun initObserveResult() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.monitoringViewState.collect { viewState ->
                    handleMonitoringViewState(viewState)
                }
            }
        }
    }

    private fun handleMonitoringViewState(viewState: ViewState<List<Monitor>>) {
        when (viewState) {
            is ViewState.Loading -> Unit
            is ViewState.Error -> {
                binding.root.showSnackBar(viewState.e.message.orEmpty())
            }
            is ViewState.Success -> monitoringAdapter.submitList(viewState.data)
        }
    }

    override fun onDestroyView() {
        viewModel.removeMonitoredDataListener()
        super.onDestroyView()
    }

    private fun initRecyclerView() {
        binding.rvMonitoring.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = monitoringAdapter
            itemAnimator = null
        }
    }
}
