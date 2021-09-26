package com.musixmatch.whosings.data.api

import com.musixmatch.whosings.data.model.*
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("track.search")
    suspend fun getTracks(
        @Query("apikey") apiKey: String = ACCESS_TOKEN,
        @Query("page_size") pageSize: String = PAGE_SIZE,
        @Query("page") page: Int,
        @Query("s_track_rating") trackRatingOrder: String
    ): Response<TrackList>

    @GET("track.lyrics.get")
    suspend fun getLyrics(
        @Query("apikey") apiKey: String = ACCESS_TOKEN,
        @Query("track_id") trackId: Int
    ): Response<LyricsWrapper>

    @GET("chart.artists.get")
    suspend fun getArtists(
        @Query("apikey") apiKey: String = ACCESS_TOKEN,
        @Query("page_size") pageSize: String = PAGE_SIZE,
        @Query("page") page: Int = 1,
        @Query("country") country: String = "it"
    ): Response<ArtistList>

}

enum class TrackOrder {
    desc,
    asc
}