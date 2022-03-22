package com.musixmatch.whosings.common.data.model.api


import com.google.gson.annotations.SerializedName

data class Track(
    @SerializedName("artist_id")
    val artistId: Int?,
    @SerializedName("artist_name")
    val artistName: String,
    @SerializedName("track_id")
    val trackId: Int,
    @SerializedName("track_name")
    val trackName: String?
)