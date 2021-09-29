package com.musixmatch.whosings.data.model.api


import com.google.gson.annotations.SerializedName

data class Lyrics(
    @SerializedName("explicit")
    val explicit: Int?,
    @SerializedName("lyrics_body")
    val lyricsBody: String,
    @SerializedName("lyrics_copyright")
    val lyricsCopyright: String?,
    @SerializedName("lyrics_id")
    val lyricsId: Int?,
    @SerializedName("pixel_tracking_url")
    val pixelTrackingUrl: String?,
    @SerializedName("script_tracking_url")
    val scriptTrackingUrl: String?,
    @SerializedName("updated_time")
    val updatedTime: String?
)