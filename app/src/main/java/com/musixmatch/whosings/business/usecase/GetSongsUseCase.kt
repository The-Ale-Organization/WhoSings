package com.musixmatch.whosings.business.usecase

import com.musixmatch.whosings.common.util.MAX_SONGS_PAGE_INDEX
import com.musixmatch.whosings.common.util.TRACKS_COUNT
import com.musixmatch.whosings.common.data.api.TrackOrder
import com.musixmatch.whosings.common.data.model.presentation.Song
import com.musixmatch.whosings.common.data.repository.MusicRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import timber.log.Timber
import javax.inject.Inject


/**
 * This use case fetches [TRACKS_COUNT] tracks. They are more than the number of questions.
 * It's done on purpose, because lyrics fetching could fail for some tracks.
 */
class GetSongsUseCase @Inject constructor(
    private val musicRepository: MusicRepository
    ) {

    suspend fun getSongs(scope: CoroutineScope): List<Song> {
        // Get tracks from api. Tracks will contain no lyrics.
        val songsWithoutLyrics = musicRepository.fetchSongs(
            page = (0..MAX_SONGS_PAGE_INDEX).random(),
            trackRatingOrder = TrackOrder.desc,
            tracksCount = TRACKS_COUNT
        )

        // Prepare one deferred for each track id.
        val deferredList = songsWithoutLyrics.map {
            scope.async {
                musicRepository.fetchLyrics(it.trackId)
            }
        }
        // Fetch all lyrics in parallel.
        deferredList.map {
            try {
                it.await()
            } catch (e: Exception) {
                // Don't stop if some lyrics are not fetched correctly.
                Timber.e(e, "One deferred failed. Continue with the others anyway.")
            }
        }

        // Now I get the all the songs with lyrics attached.
        return musicRepository.getSongsWithLyrics()
    }

}

