package com.musixmatch.whosings.data.model


import com.google.gson.annotations.SerializedName

data class TrackWrapper(
    @SerializedName("track")
    val track: Track?
)