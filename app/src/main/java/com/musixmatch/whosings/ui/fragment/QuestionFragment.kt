package com.musixmatch.whosings.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.musixmatch.whosings.data.model.UserInfo
import com.musixmatch.whosings.data.state.HomeState
import com.musixmatch.whosings.data.state.UiState
import com.musixmatch.whosings.databinding.FragmentHomeBinding
import com.musixmatch.whosings.ui.UiStateListener
import com.musixmatch.whosings.ui.viewmodel.HomeViewModel
import com.musixmatch.whosings.ui.viewmodel.QuestionViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.lang.RuntimeException


@AndroidEntryPoint
class QuestionFragment : Fragment() {

    private val viewModel: QuestionViewModel by viewModels()

    private var mUiStateListener: UiStateListener? = null

    private var _binding: FragmentHomeBinding? = null

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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()

        binding.playButton.setOnClickListener {
        }

        binding.logoutButton.setOnClickListener {

        }

        binding.recentScoresButton.setOnClickListener {

        }

        binding.rankingButton.setOnClickListener {

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
                    is HomeState.GameNotStarted -> {
                        Timber.d("State SUCCESS")
                        hideProgressBar()
                        setupUI(uiState.userInfo)
                    }
                    is HomeState.GameFinished -> {
                        Timber.d("State SUCCESS")
                        hideProgressBar()
                        setupUI(uiState.userInfo)
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

    private fun setupUI(userInfo: UserInfo) {
        binding.nameTextView.text = userInfo.username
        binding.bestScoreTextView.text = userInfo.bestScore?.toString() ?: "N.A."
        binding.rankTextView.text =
            if (userInfo.totalUsers != null && userInfo.totalUsers > 0) {
                if (userInfo.bestScore != null && userInfo.rankingPosition != null && userInfo.rankingPosition > 0) {
                    "${userInfo.rankingPosition} of ${userInfo.totalUsers}"
                } else {
                    "- of ${userInfo.totalUsers}"
                }
            } else {
                "N.A."
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
        fun newInstance() = QuestionFragment()
    }
}