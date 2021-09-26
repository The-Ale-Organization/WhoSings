package com.musixmatch.whosings.data.model

data class Song(
    val trackId: Int,
    val title: String?,
    val artist: String,
    var lyrics: String?
)
