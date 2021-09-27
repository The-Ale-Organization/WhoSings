package com.musixmatch.whosings.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.musixmatch.whosings.R
import com.musixmatch.whosings.data.model.UserInfo
import com.musixmatch.whosings.data.state.HomeState
import com.musixmatch.whosings.data.state.QuestionState
import com.musixmatch.whosings.data.state.UiState
import com.musixmatch.whosings.databinding.FragmentHomeBinding
import com.musixmatch.whosings.ui.UiStateListener
import com.musixmatch.whosings.ui.viewmodel.HomeViewModel
import com.musixmatch.whosings.ui.viewmodel.QuestionViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.lang.RuntimeException


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()
    private val questionViewModel: QuestionViewModel by activityViewModels()

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

        val initialState = questionViewModel.uiState.value
        if (initialState is QuestionState.GameFinished) {
            // We went back to home screen after finishing a game.
            homeViewModel.retrieveUserInfo(initialState.score)
        } else {
            // We haven't started a game yet.
            homeViewModel.retrieveUserInfo()
        }

        binding.playButton.setOnClickListener {
            questionViewModel.generateQuestions()
        }

        binding.logoutButton.setOnClickListener {
            homeViewModel.logout()
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
        homeViewModel.uiState.observe(viewLifecycleOwner) {
            it?.let { uiState ->
                Timber.d("State $uiState")
                when (uiState) {
                    is HomeState.UserInfoAvailable -> {
                        hideProgressBar()
                        showUserInfo(uiState.userInfo, uiState.currentScore)
                    }
                    is HomeState.Logout -> {
                        //TODO
                    }
                    is UiState.Error -> {
                        hideProgressBar()

                    }
                    is UiState.Loading -> {
                        showProgressBar()
                    }
                }
            }
        }
        questionViewModel.uiState.observe(viewLifecycleOwner) {
            it?.let { uiState ->
                Timber.d("State $uiState")
                when (uiState) {
                    is QuestionState.ShowQuestion -> {
                        hideProgressBar()
                        Timber.d("Start game!!")
                        findNavController().navigate(R.id.action_homeFragment_to_questionFragment)
                    }
                    is UiState.Error -> {
                        hideProgressBar()
                    }
                    is UiState.Loading -> {
                        showProgressBar()
                    }
                }
            }
        }
    }

    private fun showUserInfo(userInfo: UserInfo, currentScore: Int? = null) {
        if (currentScore != null) {
            // Show current score.
            binding.scoreDescriptionTextView.visibility = View.VISIBLE
            binding.scoreValueTextView.visibility = View.VISIBLE
            binding.scoreValueTextView.text = currentScore.toString()
        } else {
            // Hide current score labels.
            binding.scoreDescriptionTextView.visibility = View.INVISIBLE
            binding.scoreValueTextView.visibility = View.INVISIBLE
        }

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
        fun newInstance() = HomeFragment()
    }
}