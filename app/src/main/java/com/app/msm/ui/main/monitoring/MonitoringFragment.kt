package com.app.msm.ui.main.monitoring

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.app.msm.R
import com.app.msm.databinding.FragmentMonitoringBinding
import com.app.msm.helper.viewBinding

class MonitoringFragment : Fragment(R.layout.fragment_monitoring) {

    private val binding by viewBinding(FragmentMonitoringBinding::bind)

    private val monitoringAdapter by lazy { MonitoringAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
