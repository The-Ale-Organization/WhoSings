package com.musixmatch.whosings.data.model.presentation

data class Question(
    val lyricsLine: String,
    val answers: List<String>,
    val correctAnswerIndex: Int
)