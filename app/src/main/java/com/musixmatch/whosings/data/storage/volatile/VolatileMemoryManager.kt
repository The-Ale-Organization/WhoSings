package com.musixmatch.whosings.data.storage.volatile

import com.musixmatch.whosings.data.model.Lyrics
import com.musixmatch.whosings.data.model.Song
import com.musixmatch.whosings.data.model.Track
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VolatileMemoryManager @Inject constructor() {

    private val songList: MutableList<Song> = mutableListOf()

    fun addTracks(newTracks: List<Track>) {
        val songs = newTracks.map {
                Song(
                    trackId = it.trackId,
                    title = it.trackName,
                    artist = it.artistName,
                    lyrics = null
                )
        }
        songList.addAll(songs)
    }

    fun getSongs(): List<Song> {
        return songList
    }

    fun addLyrics(lyrics: Lyrics, trackId: Int) {
        songList.firstOrNull { it.trackId == trackId }?.let {
            it.lyrics = lyrics.lyricsBody
        }
    }

    fun clearSongs() {
        songList.clear()
    }

    fun clear() {
        clearSongs()
    }

}