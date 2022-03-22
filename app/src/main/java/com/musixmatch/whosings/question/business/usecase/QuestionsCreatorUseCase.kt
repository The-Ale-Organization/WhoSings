package com.musixmatch.whosings.question.business.usecase

import com.musixmatch.whosings.common.util.QUESTIONS_NUMBER
import com.musixmatch.whosings.common.exception.InsufficientSongsNumberException
import com.musixmatch.whosings.common.data.model.presentation.Question
import com.musixmatch.whosings.common.data.repository.MusicRepository
import javax.inject.Inject

// Number of options provided to the user.
const val POSSIBLE_ANSWERS = 3

/**
 * Generates a list of [Question].
 *
 * Each question is about the first line of a song's lyrics.
 * The user has to guess who is the singer of the song.
 * [POSSIBLE_ANSWERS] options are provided.
 * One option is correct.
 * Wrong options are randomly chosen from a list of famous artists.
 */
class QuestionsCreatorUseCase @Inject constructor(
    private val musicRepository: MusicRepository
) {

    /**
     * @param questionsCount number of questions to create
     */
    suspend fun createQuestions(questionsCount: Int = QUESTIONS_NUMBER): List<Question> {
        // Get top artist from api.
        // This is just for having some artist to use as "wrong answers" in the quiz.
        val artistList = musicRepository.fetchTopArtists()

        val songList = musicRepository.getSongsWithLyrics()

        // Song that can actually be used in the quiz.
        val validSongs = songList.filter { song ->
            // Keep only songs with at least one lyrics line which is not blank.
            song.lyrics?.split("\n")?.firstOrNull {
                it.isNotBlank()
            } != null
        }

        if (validSongs.count() < questionsCount) {
            // There are not enough songs to create the quiz.
            throw InsufficientSongsNumberException()
        }

        return validSongs
            // Don't create more questions than necessary.
            .take(questionsCount)
            // Generate a question from each valid song.
            .map { song ->
            // Randomly choose the correct answer index.
            val correctAnswerIndex = (0..POSSIBLE_ANSWERS).random()
            // Get first line of the lyrics.
            val firstLyricsLine = song.lyrics?.split("\n")?.firstOrNull {
                it.isNotBlank()
            } ?: ""

            // This list is gonna be filled with all the possible answers to the question.
            val answersList = mutableListOf<String>()

            // This list is just a helper.
            // It keeps track of which artists cannot be taken as "wrong" answers.
            // They are basically the "right" artist and artists that have already been chosen
            // in the same question, to avoid repeating the same wrong answer
            // (e.g. Shakira (correct answer), Beyonce (wrong answer), Beyonce(repeated wrong answer)).
            val forbiddenArtists = mutableListOf(song.artist)

            for (index in 0..POSSIBLE_ANSWERS) {
                if (index == correctAnswerIndex) {
                    // Put the right artist in the "correct answer" bucket
                    answersList.add(index, song.artist)
                } else {
                    // Put a random wrong artist in the "wrong answer" bucket.
                    var candidateWrongArtist: String? = null
                    while (candidateWrongArtist == null || forbiddenArtists.contains(
                            candidateWrongArtist
                        )
                    ) {
                        // Try picking a random artist from artistList.
                        // If the selected artist is not forbidden, this is the "wrong" artist we're looking for.
                        candidateWrongArtist = artistList.elementAt(
                            (artistList.indices).random()
                        ).artistName
                    }
                    forbiddenArtists.add(candidateWrongArtist)
                    answersList.add(index, candidateWrongArtist)
                }
            }

            Question(
                lyricsLine = firstLyricsLine,
                answers = answersList,
                correctAnswerIndex = correctAnswerIndex
            )
        }
    }

}

