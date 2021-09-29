package com.musixmatch.whosings.data.storage.sharedpref

import com.musixmatch.whosings.data.model.Lyrics
import com.musixmatch.whosings.data.model.Song
import com.musixmatch.whosings.data.model.Track

interface VolatileMemoryManager {

    fun addTracks(newTracks: List<Track>)

    fun getSongs(): List<Song>

    fun addLyrics(lyrics: Lyrics, trackId: Int)

    fun clearSongs()

    fun clear()
}