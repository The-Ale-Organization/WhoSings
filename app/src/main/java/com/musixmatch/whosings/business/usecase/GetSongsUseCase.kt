package com.musixmatch.whosings.business.usecase

import com.musixmatch.whosings.data.api.TrackOrder
import com.musixmatch.whosings.data.model.Song
import com.musixmatch.whosings.data.repository.MusicRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject

class GetSongsUseCase @Inject constructor(
    private val musicRepository: MusicRepository
    ) {

    suspend fun getSongs(scope: CoroutineScope): List<Song> {
        // Get tracks from api. Tracks will contain no lyrics.
        val songsWithoutLyrics = musicRepository.fetchSongs(
            page = 1,
            trackRatingOrder = TrackOrder.desc
        )

        // Prepare one deferred for each track id.
        val deferredList = songsWithoutLyrics.map {
            scope.async {
                musicRepository.fetchLyrics(it.trackId)
            }
        }
        // Fetch all lyrics in parallel.
        deferredList.map {
            it.await()
        }

        // Now I get the all the songs with lyrics attached.
        return musicRepository.getSongsWithLyrics()
    }

}

