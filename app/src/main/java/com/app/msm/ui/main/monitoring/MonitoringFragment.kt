package com.app.msm.ui.main.monitoring

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.msm.R
import com.app.msm.databinding.FragmentMonitoringBinding
import com.app.msm.helper.viewBinding
import com.app.msm.model.Control
import com.app.msm.model.Monitor
import com.app.msm.ui.main.controlling.ControlType
import com.app.msm.ui.main.controlling.ControllingViewModel

class MonitoringFragment : Fragment(R.layout.fragment_monitoring) {

    private val binding by viewBinding(FragmentMonitoringBinding::bind)

    private val viewModel: MonitoringViewModel by viewModels()

    private val monitoringAdapter by lazy { MonitoringAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initDummyData()
    }

    private fun initDummyData() {
        val listOfMonitor = listOf(
            Monitor(
                id = 1,
                label = R.string.label_suhu,
                value = "50 C"
            ),
            Monitor(
                id = 2,
                label = R.string.label_kelembapan,
                value = "50 %"
            )
        )
        monitoringAdapter.submitList(listOfMonitor)
    }

    private fun initRecyclerView() {
        binding.rvMonitoring.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = monitoringAdapter
        }
    }
}
