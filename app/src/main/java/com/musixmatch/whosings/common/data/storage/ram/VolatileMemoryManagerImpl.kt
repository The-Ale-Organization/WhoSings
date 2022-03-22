package com.musixmatch.whosings.common.data.storage.ram

import com.musixmatch.whosings.common.data.model.api.Lyrics
import com.musixmatch.whosings.common.data.model.presentation.Song
import com.musixmatch.whosings.common.data.model.api.Track

class VolatileMemoryManagerImpl : VolatileMemoryManager {

    private val songList: MutableList<Song> = mutableListOf()

    override fun addTracks(newTracks: List<Track>) {
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

    override fun getSongs(): List<Song> {
        return songList
    }

    override fun addLyrics(lyrics: Lyrics, trackId: Int) {
        songList.firstOrNull { it.trackId == trackId }?.let {
            it.lyrics = lyrics.lyricsBody
        }
    }

    override fun clearSongs() {
        songList.clear()
    }

    override fun clear() {
        clearSongs()
    }

}