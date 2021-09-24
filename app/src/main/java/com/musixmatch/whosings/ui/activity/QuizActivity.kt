package com.musixmatch.whosings.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.musixmatch.whosings.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
    }
}