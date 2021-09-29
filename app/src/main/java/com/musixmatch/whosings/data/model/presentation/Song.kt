package com.musixmatch.whosings.data.model.presentation

data class Song(
    val trackId: Int,
    val title: String?,
    val artist: String,
    var lyrics: String?
)
