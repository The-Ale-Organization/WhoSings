package com.musixmatch.whosings.data.model.api


import com.google.gson.annotations.SerializedName

data class TrackList(
    @SerializedName("track_list")
    val track: List<TrackWrapper>?
)