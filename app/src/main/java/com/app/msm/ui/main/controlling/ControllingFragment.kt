package com.app.msm.ui.main.controlling

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.app.msm.R
import com.app.msm.data.api.FirebaseProvider
import com.app.msm.databinding.FragmentControllingBinding
import com.app.msm.extension.showSnackBar
import com.app.msm.helper.viewBinding
import com.app.msm.model.Control
import com.app.msm.vo.ViewState
import kotlinx.coroutines.launch

class ControllingFragment : Fragment(R.layout.fragment_controlling) {

    private val binding by viewBinding(FragmentControllingBinding::bind)

    private val viewModel: ControllingViewModel by viewModels()

    private val controllingAdapter by lazy {
        ControllingAdapter(
            onSwitchChecked = { control -> onControllingChanged(control) },
            onButtonClicked = { control -> onControllingChanged(control) }
        )
    }

    private fun onControllingChanged(control: Control) {
        val (reference, turnOn) = when (control.id) {
            R.string.label_suhu -> {
                val controlType = control.type as ControlType.Switch
                FirebaseProvider.suhuStatusReference to controlType.isChecked
            }
            R.string.label_kelembapan -> {
                val controlType = control.type as ControlType.Switch
                FirebaseProvider.kelembapanStatusReference to controlType.isChecked
            }
            R.string.label_lampu -> {
                val controlType = control.type as ControlType.Switch
                FirebaseProvider.lampuStatusReference to controlType.isChecked
            }
            R.string.label_blower -> {
                val controlType = control.type as ControlType.Switch
                FirebaseProvider.blowerStatusReference to controlType.isChecked
            }
            R.string.label_download_laporan -> return
            else -> return
        }
        viewModel.updateControllingData(reference, turnOn)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initObserveResult()
        viewModel.listenToControllingData()
    }

    private fun initObserveResult() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.controllingViewState.collect { viewState ->
                    handleControllingViewState(viewState)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.controllingActionState.collect { actionState ->
                    handleControllingActionState(actionState)
                }
            }
        }
    }

    private fun handleControllingActionState(actionState: ViewState<Boolean>) {
        when (actionState) {
            is ViewState.Loading -> Unit
            is ViewState.Error -> {
                binding.root.showSnackBar(actionState.e.message.orEmpty())
            }
            is ViewState.Success -> {
                val message = if (actionState.data) "Berhasil mengaktifkan" else "Berhasil mematikan"
                binding.root.showSnackBar(message)
            }
        }
    }

    private fun handleControllingViewState(viewState: ViewState<List<Control>>) {
        when (viewState) {
            is ViewState.Loading -> Unit
            is ViewState.Error -> {
                binding.root.showSnackBar(viewState.e.message.orEmpty())
            }
            is ViewState.Success -> controllingAdapter.submitList(viewState.data)
        }
    }

    override fun onDestroyView() {
        viewModel.removeControllingDataListener()
        super.onDestroyView()
    }

    private fun initRecyclerView() {
        binding.rvControlling.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = controllingAdapter
            itemAnimator = null
        }
    }
}
