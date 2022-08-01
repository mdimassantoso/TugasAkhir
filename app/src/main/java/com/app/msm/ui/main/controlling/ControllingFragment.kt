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
import com.app.msm.model.controlling.Control
import com.app.msm.vo.ViewState
import kotlinx.coroutines.launch

class ControllingFragment : Fragment(R.layout.fragment_controlling) {

    private val binding by viewBinding(FragmentControllingBinding::bind)

    private val viewModel: ControllingViewModel by viewModels()

    private val controllingAdapter by lazy {
        ControllingAdapter(
            onSwitchChecked = { id, isChecked -> onSwitchChanged(id, isChecked) },
            onButtonClicked = { id -> onControllingChanged(id) }
        )
    }

    private fun onSwitchChanged(id: Int, isChecked: Boolean) {
        val reference = when (id) {
            R.string.label_watering -> FirebaseProvider.wateringControlReference
            R.string.label_blower -> FirebaseProvider.blowerControlReference
            R.string.label_lampu -> FirebaseProvider.lampControlReference
            else -> return
        }
        viewModel.updateControllingData(reference, isChecked)
    }

    private fun onControllingChanged(id: Int) {
        when (id) {
            R.string.label_view_image -> Unit // TODO get url & show image preview dialog
            else -> return
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initObserveResult()
        initListener()
        listenToFirebase()
    }

    private fun listenToFirebase() {
        viewModel.listenToAutoConfig()
        viewModel.listenToControlling()
    }

    private fun initListener() = with(binding) {
        swAutomatic.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateAutomaticConfiguration(isChecked)
        }
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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.automaticSwitchViewState.collect { viewState ->
                    handleAutomaticViewState(viewState)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.automaticSwitchActionState.collect { actionState ->
                    handleAutomaticActionState(actionState)
                }
            }
        }
    }

    private fun handleAutomaticActionState(actionState: ViewState<Boolean>) = with(binding) {
        swAutomatic.isEnabled = actionState !is ViewState.Loading
        when (actionState) {
            is ViewState.Loading -> Unit
            is ViewState.Error -> {
                root.showSnackBar(actionState.e.message.orEmpty())
            }
            is ViewState.Success -> {
                val message = if (actionState.data) "Berhasil mengaktifkan" else "Berhasil mematikan"
                root.showSnackBar(message)
            }
        }
    }

    private fun handleAutomaticViewState(viewState: ViewState<Boolean>) = with(binding) {
        swAutomatic.isEnabled = viewState !is ViewState.Loading
        when (viewState) {
            is ViewState.Loading -> Unit
            is ViewState.Error -> {
                root.showSnackBar(viewState.e.message.orEmpty())
            }
            is ViewState.Success -> swAutomatic.isChecked = viewState.data
        }
    }

    private fun handleControllingActionState(actionState: ViewState<Boolean>) = with(binding) {
        controllingAdapter.preventAction = actionState is ViewState.Loading
        when (actionState) {
            is ViewState.Loading -> Unit
            is ViewState.Error -> {
                root.showSnackBar(actionState.e.message.orEmpty())
            }
            is ViewState.Success -> {
                val message = if (actionState.data) "Berhasil mengaktifkan" else "Berhasil mematikan"
                root.showSnackBar(message)
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
        removeFirebaseListener()
        super.onDestroyView()
    }

    private fun removeFirebaseListener() {
        viewModel.removeAutoConfigListener()
        viewModel.removeControllingListener()
    }

    private fun initRecyclerView() {
        binding.rvControlling.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = controllingAdapter
            itemAnimator = null
        }
    }
}
