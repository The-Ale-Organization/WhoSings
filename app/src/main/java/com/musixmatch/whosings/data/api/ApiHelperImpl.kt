package com.musixmatch.whosings.data.api

import com.musixmatch.whosings.data.model.api.ArtistList
import com.musixmatch.whosings.data.model.api.LyricsWrapper
import com.musixmatch.whosings.data.model.api.TrackList
import javax.inject.Inject


class ApiHelperImpl @Inject constructor(
    private val apiService: ApiService
) : ApiHelper {

    override suspend fun getTracks(
        page: Int,
        trackRatingOrder: TrackOrder,
        tracksCount: Int
    ): TrackList {
        val response = apiService.getTracks(
            page = page,
            pageSize = tracksCount.toString(),
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