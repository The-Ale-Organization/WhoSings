package com.musixmatch.whosings.data.model


import com.google.gson.annotations.SerializedName

data class Artist(
    @SerializedName("artist_alias_list")
    val artistAliasList: List<Any>?,
    @SerializedName("artist_id")
    val artistId: Int?,
    @SerializedName("artist_mbid")
    val artistMbid: String?,
    @SerializedName("artist_name")
    val artistName: String,
    @SerializedName("artist_rating")
    val artistRating: Int?,
    @SerializedName("updated_time")
    val updatedTime: String?
)