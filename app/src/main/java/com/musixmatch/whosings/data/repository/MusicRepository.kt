package com.musixmatch.whosings.data.repository

import com.musixmatch.whosings.data.api.TrackOrder
import com.musixmatch.whosings.data.model.api.Artist
import com.musixmatch.whosings.data.model.api.Lyrics
import com.musixmatch.whosings.data.model.presentation.Song

interface MusicRepository {

    suspend fun fetchSongs(page: Int, trackRatingOrder: TrackOrder): List<Song>

    suspend fun fetchLyrics(trackId: Int): Lyrics?

    fun getSongsWithLyrics(): List<Song>

    suspend fun fetchTopArtists(): List<Artist>

    fun clearSongs()

    fun clearSessionData()

}