package com.musixmatch.whosings.data.api

import com.musixmatch.whosings.data.model.ArtistList
import com.musixmatch.whosings.data.model.LyricsWrapper
import com.musixmatch.whosings.data.model.TrackList
import javax.inject.Inject


class ApiHelperImpl @Inject constructor(
    private val apiService: ApiService
) : ApiHelper {

    override suspend fun getTracks(
        page: Int,
        trackRatingOrder: TrackOrder
    ): TrackList {
        val response = apiService.getTracks(
            page = page,
            trackRatingOrder = trackRatingOrder.toString()
        )
        return ResponseParser().parse(response)
    }

    override suspend fun getLyrics(
        trackId: Int
    ): LyricsWrapper {
        val response = apiService.getLyrics(
            trackId = trackId
        )
        return ResponseParser().parse(response)
    }

    override suspend fun getTopArtists(): ArtistList {
        val response = apiService.getArtists()
        return ResponseParser().parse(response)
    }
}