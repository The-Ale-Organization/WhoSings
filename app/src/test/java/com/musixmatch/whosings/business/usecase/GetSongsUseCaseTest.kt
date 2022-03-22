package com.musixmatch.whosings.business.usecase

import com.musixmatch.whosings.common.data.model.api.Lyrics
import com.musixmatch.whosings.common.data.model.presentation.Song
import com.musixmatch.whosings.common.data.repository.MusicRepository
import com.musixmatch.whosings.question.business.usecase.GetSongsUseCase
import com.musixmatch.whosings.shared.CoroutineTestRule
import com.musixmatch.whosings.shared.MockitoHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.junit.Rule
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito
import org.mockito.Mockito.times

@ExperimentalCoroutinesApi
class GetSongsUseCaseTest {

    // Set the coroutines dispatcher for unit testing
    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    private val mockMusicRepository = Mockito.mock(MusicRepository::class.java)
    private val useCase = GetSongsUseCase(mockMusicRepository)

    private val songsWithoutLyrics = listOf(
        Song(
            trackId = 0,
            title = "Title 1",
            artist = "Artist 1",
            lyrics = null

        )
    )

    private val songs = listOf(
        Song(
            trackId = 0,
            title = "Title 1",
            artist = "Artist 1",
            lyrics = "bla bla bla"

        )
    )

    private val lyrics = Lyrics(
            explicit = null,
            lyricsBody = "",
            lyricsCopyright = null,
            lyricsId = null,
            pixelTrackingUrl = null,
            scriptTrackingUrl = null,
            updatedTime = null
        )


    @Test
    fun getSongs() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(mockMusicRepository.fetchSongs(
            page = anyInt(),
            trackRatingOrder = MockitoHelper.anyObject(),
            tracksCount = anyInt()
        )).thenReturn(songsWithoutLyrics)

        Mockito.`when`(mockMusicRepository.fetchLyrics(
            trackId = anyInt()
        )).thenReturn(lyrics)

        Mockito.`when`(mockMusicRepository.getSongsWithLyrics())
            .thenReturn(songs)

        useCase.getSongs(this)

        Mockito.verify(mockMusicRepository, times(songsWithoutLyrics.count())).fetchLyrics(anyInt())
    }
}