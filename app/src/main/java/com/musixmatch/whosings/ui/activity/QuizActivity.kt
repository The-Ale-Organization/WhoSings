package com.musixmatch.whosings.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.musixmatch.whosings.business.error.ErrorHandler
import com.musixmatch.whosings.databinding.ActivityQuizBinding
import com.musixmatch.whosings.ui.UiStateListener
import com.musixmatch.whosings.ui.fragment.ErrorDialog
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class QuizActivity : AppCompatActivity(), UiStateListener {

    private var _binding: ActivityQuizBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.progressBar.visibility = View.GONE
    }

    override fun showError(uiError: ErrorHandler.UIError) {
        ErrorDialog().show(
            supportFragmentManager, "TAG")
    }
}