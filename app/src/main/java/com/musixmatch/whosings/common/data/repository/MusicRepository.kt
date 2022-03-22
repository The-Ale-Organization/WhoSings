package com.musixmatch.whosings.common.data.repository

import com.musixmatch.whosings.common.data.api.TrackOrder
import com.musixmatch.whosings.common.data.model.api.Artist
import com.musixmatch.whosings.common.data.model.api.Lyrics
import com.musixmatch.whosings.common.data.model.presentation.Song

interface MusicRepository {

    suspend fun fetchSongs(page: Int, trackRatingOrder: TrackOrder, tracksCount: Int): List<Song>

    suspend fun fetchLyrics(trackId: Int): Lyrics?

    fun getSongsWithLyrics(): List<Song>

    suspend fun fetchTopArtists(): List<Artist>

    fun clearSongs()

}