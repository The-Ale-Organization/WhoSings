package com.musixmatch.whosings.presentation.navigation

import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import com.musixmatch.whosings.R
import javax.inject.Inject

class LoginNavigator @Inject constructor(
    private val activity: FragmentActivity
) {

    fun navigateToHome() {
        activity.findNavController(R.id.fragmentContainerView)
            .navigate(R.id.action_introFragment_to_homeFragment)
    }
}