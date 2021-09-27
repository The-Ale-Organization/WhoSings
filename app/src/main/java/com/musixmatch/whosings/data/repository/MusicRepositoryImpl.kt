package com.musixmatch.whosings.data.repository

import com.musixmatch.whosings.data.api.ApiHelper
import com.musixmatch.whosings.data.api.TrackOrder
import com.musixmatch.whosings.data.model.Artist
import com.musixmatch.whosings.data.model.Lyrics
import com.musixmatch.whosings.data.model.Song
import com.musixmatch.whosings.data.storage.volatile.VolatileMemoryManager
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(
    private val apiHelper: ApiHelper,
    private val volatileMemoryManager: VolatileMemoryManager
) : MusicRepository {

    override suspend fun fetchSongs(page: Int, trackRatingOrder: TrackOrder): List<Song> {
        val response = apiHelper.getTracks(
            page = page,
            trackRatingOrder = trackRatingOrder
        )
        // Save tracks to volatile storage.
        val tracks = response.track?.mapNotNull { it.track }?.toList() ?: listOf()
        volatileMemoryManager.addTracks(tracks)
        // Return the complete list of tracks.
        return volatileMemoryManager.getSongs()
    }

    override suspend fun fetchLyrics(trackId: Int): Lyrics? {
        val response = apiHelper.getLyrics(trackId)
        // Save lyrics to volatile storage.
        response.lyrics?.let {
            volatileMemoryManager.addLyrics(it, trackId)
        }
        return response.lyrics
    }

    override fun getSongsWithLyrics(): List<Song> {
        return volatileMemoryManager.getSongs()
    }

    override suspend fun fetchTopArtists(): List<Artist> {
        val response = apiHelper.getTopArtists()
        return response.artistList.map { it .artist }
    }

    override fun clearSongs() {
        volatileMemoryManager.clearSongs()
    }

    override fun clearSessionData() {
        volatileMemoryManager.clear()
    }

}