package com.musixmatch.whosings.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.musixmatch.whosings.data.state.LoginState
import com.musixmatch.whosings.data.state.UiState
import com.musixmatch.whosings.databinding.FragmentLoginBinding
import com.musixmatch.whosings.ui.UiStateListener
import com.musixmatch.whosings.ui.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.lang.RuntimeException


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

    private var mUiStateListener: UiStateListener? = null

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is UiStateListener) {
            mUiStateListener = context
        } else {
            throw RuntimeException("${context::javaClass.name} must implement ${UiStateListener::javaClass.name}")
        }
    }

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

        binding.signInButton.setOnClickListener {
            viewModel.register(binding.userTextField.editText?.text.toString())

        }

        binding.signUpButton.setOnClickListener {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mUiStateListener = null
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
                        hideProgressBar()
                    }
                    is UiState.Error -> {
                        Timber.d("State ERROR")
                        hideProgressBar()

                    }
                    is UiState.Loading -> {
                        Timber.d("State LOADING")
                        showProgressBar()
                    }
                }
            }
        }
    }

    private fun showProgressBar() {
        // Hide root view.
        binding.root.visibility = View.GONE
        // Show progress bar.
        mUiStateListener?.showProgress()
    }

    private fun hideProgressBar() {
        // Show root view.
        binding.root.visibility = View.VISIBLE
        // Hide progress bar
        mUiStateListener?.hideProgress()
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}