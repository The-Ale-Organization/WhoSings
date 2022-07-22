package com.musixmatch.whosings.presentation.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.musixmatch.whosings.R
import com.musixmatch.whosings.data.model.presentation.Question
import com.musixmatch.whosings.data.state.QuestionState
import com.musixmatch.whosings.data.state.TimerState
import com.musixmatch.whosings.data.state.UiState
import com.musixmatch.whosings.databinding.FragmentQuestionBinding
import com.musixmatch.whosings.presentation.UiStateListener
import com.musixmatch.whosings.presentation.viewmodel.QuestionViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.lang.RuntimeException


@AndroidEntryPoint
class QuestionFragment : Fragment() {

    private val questionViewModel: QuestionViewModel by activityViewModels()

    private var mUiStateListener: UiStateListener? = null

    private var _binding: FragmentQuestionBinding? = null

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
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()

        binding.optionOneButton.setOnClickListener {
            questionViewModel.processAnswer(0)
        }
        binding.optionTwoButton.setOnClickListener {
            questionViewModel.processAnswer(1)
        }
        binding.optionThreeButton.setOnClickListener {
            questionViewModel.processAnswer(2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mUiStateListener = null
    }

    private fun setupObservers() {
        questionViewModel.uiState.observe(viewLifecycleOwner) {
            it?.let { uiState ->
                Timber.d("State $uiState")
                when (uiState) {
                    is QuestionState.ShowQuestion -> {
                        hideProgressBar()
                        Timber.d("Show question ${uiState.questionIndex +1} of ${uiState.totalQuestions}")
                        // Start countdown.
                        questionViewModel.startCountDown()
                        // Update UI.
                        binding.scoreValueTextView.text = uiState.currentScore.toString()
                        binding.questionCounterTextView.text = getString(
                            R.string.question_counter,
                            (uiState.questionIndex +1).toString(),
                            uiState.totalQuestions.toString())

                        showQuestion(uiState.question)
                    }
                    is QuestionState.GameFinished -> {
                        hideProgressBar()
                        Timber.d("End of game")
                        findNavController().navigate(R.id.action_questionFragment_to_homeFragment)
                    }
                    is UiState.Error -> {
                        hideProgressBar()
                        mUiStateListener?.showError(uiState.type)
                    }
                    is UiState.Loading -> {
                        showProgressBar()
                    }
                    else -> {}
                }
            }
        }

        questionViewModel.timerStatus.observe(viewLifecycleOwner) {
            when(it) {
                is TimerState.Tick -> {
                    binding.circularProgressIndicator.progress = it.progress
                    binding.countdownValueTextView.text = it.remainingTime.toString()
                }
                TimerState.Timeout -> {
                    questionViewModel.processAnswer(null)
                }
            }
        }
    }

    private fun showQuestion(question: Question) {
        hideProgressBar()
        binding.questionTextView.text = getString(R.string.question, question.lyricsLine)
        binding.optionOneButton.text = question.answers[0]
        binding.optionTwoButton.text = question.answers[1]
        binding.optionThreeButton.text = question.answers[2]
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