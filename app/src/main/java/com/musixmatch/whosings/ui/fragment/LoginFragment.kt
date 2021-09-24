package com.musixmatch.whosings.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.musixmatch.whosings.data.state.LoginState
import com.musixmatch.whosings.databinding.FragmentLoginBinding
import com.musixmatch.whosings.ui.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupUI()
        setupObservers()

        binding.enterButton.setOnClickListener {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupViewModel() {
    }

    private fun setupUI() {
    }

    private fun setupObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            it?.let { uiState ->
                when (uiState) {
                    is LoginState.Success -> {
                        Timber.d("State SUCCESS")
                        // Show root view.
                    }
                    is LoginState.Error -> {
                        Timber.d("State ERROR")
                        // Show root view.
                    }
                    is LoginState.Loading -> {
                        Timber.d("State LOADING")
                        // Hide root view.
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}