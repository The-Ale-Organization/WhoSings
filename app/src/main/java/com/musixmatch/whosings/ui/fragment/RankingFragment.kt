package com.musixmatch.whosings.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.musixmatch.whosings.R
import com.musixmatch.whosings.data.state.RankingState
import com.musixmatch.whosings.data.state.UiState
import com.musixmatch.whosings.databinding.FragmentRankingBinding
import com.musixmatch.whosings.ui.UiStateListener
import com.musixmatch.whosings.ui.viewmodel.RankingViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.lang.RuntimeException


@AndroidEntryPoint
class RankingFragment : Fragment() {

    private val rankingViewModel: RankingViewModel by viewModels()

    private var mUiStateListener: UiStateListener? = null

    private var _binding: FragmentRankingBinding? = null

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
        _binding = FragmentRankingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()

        binding.backButton.setOnClickListener {
            // Go back to home.
            findNavController().navigate(R.id.action_rankingFragment_to_homeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mUiStateListener = null
    }

    private fun setupObservers() {
        rankingViewModel.uiState.observe(viewLifecycleOwner) {
            it?.let { uiState ->
                Timber.d("State $uiState")
                when (uiState) {
                    is RankingState.RankAvailable -> {
                        hideProgressBar()
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
        fun newInstance() = RankingFragment()
    }
}