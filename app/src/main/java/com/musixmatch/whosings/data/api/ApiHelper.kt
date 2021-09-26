package com.musixmatch.whosings.data.api

import com.musixmatch.whosings.data.model.ArtistList
import com.musixmatch.whosings.data.model.LyricsWrapper
import com.musixmatch.whosings.data.model.TrackList

interface ApiHelper {

    suspend fun getTracks(
        page: Int,
        trackRatingOrder: TrackOrder
    ): TrackList

    suspend fun getLyrics(
        trackId: Int
    ): LyricsWrapper

    suspend fun getTopArtists(): ArtistList

}