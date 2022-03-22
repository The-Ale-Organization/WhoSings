package com.musixmatch.whosings.history.presentation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.musixmatch.whosings.R
import com.musixmatch.whosings.data.model.presentation.RecentGameItem
import com.musixmatch.whosings.data.state.UiState
import com.musixmatch.whosings.data.state.UserHistoryState
import com.musixmatch.whosings.databinding.FragmentUserHistoryBinding
import com.musixmatch.whosings.presentation.UiStateListener
import com.musixmatch.whosings.history.presentation.adapter.UserHistoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.lang.RuntimeException


@AndroidEntryPoint
class UserHistoryFragment : Fragment() {

    private val rankingViewModel: UserHistoryViewModel by viewModels()

    private var mUiStateListener: UiStateListener? = null

    private var _binding: FragmentUserHistoryBinding? = null

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
        _binding = FragmentUserHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()

        rankingViewModel.getRecentGamesScores()

        binding.backButton.setOnClickListener {
            // Go back to home.
            findNavController().navigate(R.id.action_userHistoryFragment_pop)
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
                    is UserHistoryState.RecentGamesAvailable -> {
                        hideProgressBar()
                        showList(uiState.items)
                    }
                    is UiState.Error -> {
                        hideProgressBar()
                        mUiStateListener?.showError(uiState.type)
                    }
                    is UiState.Loading -> {
                        showProgressBar()
                    }
                }
            }
        }
    }

    private fun showList(items: List<RecentGameItem>) {
        if (items.isEmpty()) {
            binding.noItemsTextView.visibility = View.VISIBLE
            binding.scoreIndicator.visibility = View.GONE
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.noItemsTextView.visibility = View.GONE
            binding.scoreIndicator.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.VISIBLE
            val layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerView.layoutManager = layoutManager
            val adapter = UserHistoryAdapter(items)
            binding.recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
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
        fun newInstance() = UserHistoryFragment()
    }
}