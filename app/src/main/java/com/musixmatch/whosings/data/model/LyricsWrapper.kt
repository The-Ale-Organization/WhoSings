package com.musixmatch.whosings.data.model

import com.google.gson.annotations.SerializedName

data class LyricsWrapper(
    @SerializedName("lyrics")
    val lyrics: Lyrics?
)
