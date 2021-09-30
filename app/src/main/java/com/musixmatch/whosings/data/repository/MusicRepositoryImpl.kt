package com.musixmatch.whosings.data.repository

import com.musixmatch.whosings.data.api.ApiHelper
import com.musixmatch.whosings.data.api.TrackOrder
import com.musixmatch.whosings.data.model.api.Artist
import com.musixmatch.whosings.data.model.api.Lyrics
import com.musixmatch.whosings.data.model.presentation.Song
import com.musixmatch.whosings.data.storage.ram.VolatileMemoryManager
import timber.log.Timber
import javax.inject.Inject

class MusicRepositoryImpl constructor(
    private val apiHelper: ApiHelper,
    private val volatileMemoryManager: VolatileMemoryManager
) : MusicRepository {

    override suspend fun fetchSongs(page: Int, trackRatingOrder: TrackOrder, tracksCount: Int): List<Song> {
        val response = apiHelper.getTracks(
            page = page,
            trackRatingOrder = trackRatingOrder,
            tracksCount = tracksCount
        )
        // Save tracks to volatile storage.
        val tracks = response.track?.mapNotNull { it.track }?.toList() ?: listOf()
        volatileMemoryManager.addTracks(tracks)
        // Return the complete list of tracks.
        return volatileMemoryManager.getSongs()
    }

    override suspend fun fetchLyrics(trackId: Int): Lyrics? {
        Timber.d("GET LYRICS $trackId --->")
        val response = apiHelper.getLyrics(trackId)
        Timber.d("GET LYRICS $trackId <---")
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

}