package com.musixmatch.whosings.data.model.api


import com.google.gson.annotations.SerializedName

data class ArtistList(
    @SerializedName("artist_list")
    val artistList: List<ArtistWrapper>
)