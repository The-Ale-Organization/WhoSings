package com.musixmatch.whosings.common.data.model.api

import com.google.gson.annotations.SerializedName

data class LyricsWrapper(
    @SerializedName("lyrics")
    val lyrics: Lyrics?
)
