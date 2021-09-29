package com.musixmatch.whosings.business.usecase

import com.musixmatch.whosings.data.model.api.Artist
import com.musixmatch.whosings.data.model.presentation.Song
import com.musixmatch.whosings.data.repository.MusicRepository
import com.musixmatch.whosings.shared.CoroutineTestRule
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.hasItems
import org.hamcrest.MatcherAssert
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.contains
import org.mockito.Mockito

class QuestionsCreatorUseCaseTest {

    // Set the coroutines dispatcher for unit testing
    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    private val mockMusicRepository = Mockito.mock(MusicRepository::class.java)
    private val useCase = QuestionsCreatorUseCase(mockMusicRepository)

    private val artists = listOf(
        Artist(
            artistAliasList = listOf(),
            artistId = null,
            artistMbid = null,
            artistName = "Giusy Ferreri",
            artistRating = null,
            updatedTime = null
        ),
        Artist(
            artistAliasList = listOf(),
            artistId = null,
            artistMbid = null,
            artistName = "Shakira",
            artistRating = null,
            updatedTime = null
        ),
        Artist(
            artistAliasList = listOf(),
            artistId = null,
            artistMbid = null,
            artistName = "Eros Ramazzotti",
            artistRating = null,
            updatedTime = null
        ),
        Artist(
            artistAliasList = listOf(),
            artistId = null,
            artistMbid = null,
            artistName = "Jay-z",
            artistRating = null,
            updatedTime = null
        )
    )

    private val songs = listOf(
        Song(
            trackId = 0,
            title = "Title 1",
            artist = "John Lennon",
            lyrics = "bla bla bla\nagain bla bla bla\nthird time bla bla"
        ),
        Song(
            trackId = 1,
            title = "We are the champions",
            artist = "The Queen",
            lyrics = "I've paid my dues\nTime after time\nI've done my sentence"
        )
    )


    @Test
    fun question_contains_correct_answer() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(mockMusicRepository.fetchTopArtists())
            .thenReturn(artists)
        Mockito.`when`(mockMusicRepository.getSongsWithLyrics())
            .thenReturn(songs)

        val questions = useCase.createQuestions()

        assertEquals(questions.size, songs.size)

        val q1 = questions[0]
        val q2 = questions[1]

        //assertEquals(q1.lyricsLine, "bla bla bla\nagain bla bla bla\nthird time bla bla")
        MatcherAssert.assertThat(q1.answers, hasItems("John Lennon"))

        MatcherAssert.assertThat(q2.answers, hasItems("The Queen"))
    }

    @Test
    fun question_has_correct_first_line_of_lyrics() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(mockMusicRepository.fetchTopArtists())
            .thenReturn(artists)
        Mockito.`when`(mockMusicRepository.getSongsWithLyrics())
            .thenReturn(songs)

        val questions = useCase.createQuestions()
        val q1 = questions[0]
        val q2 = questions[1]

        assertEquals(q1.lyricsLine, "bla bla bla")
        assertEquals(q2.lyricsLine, "I've paid my dues")
    }

    @Test
    fun question_no_duplicate_answers() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(mockMusicRepository.fetchTopArtists())
            .thenReturn(artists)
        Mockito.`when`(mockMusicRepository.getSongsWithLyrics())
            .thenReturn(songs)

        val questions = useCase.createQuestions()
        val q1 = questions[0]
        val q2 = questions[1]

        assertEquals(q1.answers.toHashSet().count(), q1.answers.count())
        assertEquals(q2.answers.toHashSet().count(), q2.answers.count())
    }

    @Test
    fun question_correct_answers_index_is_ok() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Mockito.`when`(mockMusicRepository.fetchTopArtists())
            .thenReturn(artists)
        Mockito.`when`(mockMusicRepository.getSongsWithLyrics())
            .thenReturn(songs)

        val questions = useCase.createQuestions()
        val q1 = questions[0]
        val q2 = questions[1]

        assertEquals("John Lennon", q1.answers[q1.correctAnswerIndex])
        assertEquals("The Queen", q2.answers[q2.correctAnswerIndex])
    }
}