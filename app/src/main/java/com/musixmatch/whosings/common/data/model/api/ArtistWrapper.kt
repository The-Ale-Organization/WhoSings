package com.musixmatch.whosings.common.data.model.api


import com.google.gson.annotations.SerializedName

data class ArtistWrapper(
    @SerializedName("artist")
    val artist: Artist
)