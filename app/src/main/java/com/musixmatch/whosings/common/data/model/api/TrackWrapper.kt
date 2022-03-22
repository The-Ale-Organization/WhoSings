package com.musixmatch.whosings.common.data.model.api


import com.google.gson.annotations.SerializedName

data class TrackWrapper(
    @SerializedName("track")
    val track: Track?
)