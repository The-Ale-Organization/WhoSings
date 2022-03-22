package com.musixmatch.whosings.common.data.model.api


import com.google.gson.annotations.SerializedName

data class TrackList(
    @SerializedName("track_list")
    val track: List<TrackWrapper>?
)