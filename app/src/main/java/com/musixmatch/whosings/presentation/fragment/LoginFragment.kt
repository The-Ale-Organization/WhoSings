package com.musixmatch.whosings.presentation.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.musixmatch.whosings.R
import com.musixmatch.whosings.business.error.ErrorHandler
import com.musixmatch.whosings.data.state.LoginState
import com.musixmatch.whosings.data.state.UiState
import com.musixmatch.whosings.databinding.FragmentLoginBinding
import com.musixmatch.whosings.presentation.UiStateListener
import com.musixmatch.whosings.presentation.viewmodel.LoginViewModel
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
            throw RuntimeException("${context::class.java.simpleName} must implement ${UiStateListener::class.java.simpleName}")
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

        setupObservers()

        binding.signInButton.setOnClickListener {
            viewModel.login(binding.userTextField.editText?.text.toString())

        }

        binding.signUpButton.setOnClickListener {
            viewModel.register(binding.userTextField.editText?.text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mUiStateListener = null
    }

    private fun setupObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            it?.let { uiState ->
                when (uiState) {
                    is LoginState.LoggedIn -> {
                        Timber.d("State SUCCESS")
                        hideProgressBar()
                        binding.userTextField.error = null
                        findNavController().navigate(R.id.action_introFragment_to_homeFragment)
                    }
                    is UiState.Error -> {
                        Timber.d("State ERROR")
                        hideProgressBar()
                        when (uiState.type) {
                            ErrorHandler.UIError.UserNotFound -> {
                                binding.userTextField.error = getString(R.string.error_not_registered)
                            }
                            ErrorHandler.UIError.AlreadyRegistered -> {
                                binding.userTextField.error = getString(R.string.error_already_registered)
                            }
                            ErrorHandler.UIError.EmptyField -> {
                                binding.userTextField.error = getString(R.string.error_empty_username)
                            }
                            else -> {
                                binding.userTextField.error = null
                                mUiStateListener?.showError(uiState.type)
                            }
                        }
                    }
                    is UiState.Loading -> {
                        Timber.d("State LOADING")
                        binding.userTextField.error = null
                        showProgressBar()
                    }
                    else -> {}
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