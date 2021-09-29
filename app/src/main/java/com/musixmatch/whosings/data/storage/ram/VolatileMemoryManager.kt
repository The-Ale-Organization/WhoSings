package com.musixmatch.whosings.data.storage.ram

import com.musixmatch.whosings.data.model.api.Lyrics
import com.musixmatch.whosings.data.model.presentation.Song
import com.musixmatch.whosings.data.model.api.Track

interface VolatileMemoryManager {

    fun addTracks(newTracks: List<Track>)

    fun getSongs(): List<Song>

    fun addLyrics(lyrics: Lyrics, trackId: Int)

    fun clearSongs()

    fun clear()
}