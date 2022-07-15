package com.musixmatch.whosings.data.api

import com.musixmatch.whosings.data.model.api.ArtistList
import com.musixmatch.whosings.data.model.api.LyricsWrapper
import com.musixmatch.whosings.data.model.api.TrackList

interface ApiHelper {

    suspend fun getTracks(
        page: Int,
        trackRatingOrder: TrackOrder,
        tracksCount: Int
    ): TrackList

    suspend fun getLyrics(
        trackId: Int
    ): LyricsWrapper

    suspend fun getTopArtists(): ArtistList

}
//blablabla