package com.musixmatch.whosings.login.presentation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.musixmatch.whosings.R
import com.musixmatch.whosings.common.business.error.ErrorHandler
import com.musixmatch.whosings.databinding.FragmentLoginBinding
import com.musixmatch.whosings.common.presentation.UiStateListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
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
            viewModel.setEvent(
                LoginContract.Event.SignInClicked(
                username = binding.userTextField.editText?.text.toString()
            ))
        }

        binding.signUpButton.setOnClickListener {
            viewModel.setEvent(
                LoginContract.Event.RegisterClicked(
                username = binding.userTextField.editText?.text.toString()
            ))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mUiStateListener = null
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenStarted {
            // Collect UI state
            viewModel.uiState.collect {
                binding.userTextField.error = null
                if (it.isLoading) {
                    showProgressBar()
                } else {
                    hideProgressBar()
                }

                if (it.error != null) {
                    handleError(it.error)
                } else {
                    binding.userTextField.error = null
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            // Collect side effect
            viewModel.effect.collect {
                when (it) {
                    LoginContract.Effect.ShowToast -> {
                        //TODO
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

    private fun handleError(error: ErrorHandler.UIError) {
        when (error) {
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
                mUiStateListener?.showError(error)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}