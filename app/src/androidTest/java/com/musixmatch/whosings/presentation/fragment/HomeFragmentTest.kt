package com.musixmatch.whosings.presentation.fragment

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import com.musixmatch.whosings.R
import com.whosings.base_test.launchFragmentInHiltContainer


@HiltAndroidTest
internal class HomeFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)


    @Test
    fun test() {
        launchFragmentInHiltContainer<HomeFragment>().use {
            onView(withId(R.id.nameTextView)).check(matches(withText("Mark")))
        }
    }


}