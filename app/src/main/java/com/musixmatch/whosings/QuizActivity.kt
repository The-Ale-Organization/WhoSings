package com.musixmatch.whosings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.musixmatch.whosings.common.business.error.ErrorHandler
import com.musixmatch.whosings.databinding.ActivityQuizBinding
import com.musixmatch.whosings.common.presentation.UiStateListener
import com.musixmatch.whosings.common.presentation.dialog.ErrorDialog
import com.musixmatch.whosings.login.navigation.LoginNavigator
import com.musixmatch.whosings.common.presentation.navigation.NavigationDispatcher
import com.musixmatch.whosings.common.presentation.navigation.Route
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class QuizActivity : AppCompatActivity(), UiStateListener {

    private var _binding: ActivityQuizBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var navDispatcher: NavigationDispatcher

    @Inject
    lateinit var loginRoutes: LoginNavigator



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Timber.d("Observing on dispatcher ${System.identityHashCode(navDispatcher)}")
        lifecycleScope.launchWhenStarted {
            observeNavigationChanges()
        }

        lifecycleScope.launchWhenStarted {
            navDispatcher.flow1.collect {
                Log.d("QuizActivity", "launchWhenStarted collected $it on coroutine ${System.identityHashCode(this)}")
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                navDispatcher.flow2.collect {
                    Log.d("QuizActivity", "repeatOnLifecycle collected $it on coroutine ${System.identityHashCode(this)}")
                }
            }

        }
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

    private suspend fun observeNavigationChanges() {
        navDispatcher.navigationChangesFlow.collect {
            when (it) {
                Route.Home -> loginRoutes.navigateToHome()
            }
        }
    }
}