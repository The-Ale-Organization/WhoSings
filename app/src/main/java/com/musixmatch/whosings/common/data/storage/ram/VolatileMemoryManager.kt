package com.musixmatch.whosings.common.data.storage.ram

import com.musixmatch.whosings.common.data.model.api.Lyrics
import com.musixmatch.whosings.common.data.model.presentation.Song
import com.musixmatch.whosings.common.data.model.api.Track

interface VolatileMemoryManager {

    fun addTracks(newTracks: List<Track>)

    fun getSongs(): List<Song>

    fun addLyrics(lyrics: Lyrics, trackId: Int)

    fun clearSongs()

    fun clear()
}