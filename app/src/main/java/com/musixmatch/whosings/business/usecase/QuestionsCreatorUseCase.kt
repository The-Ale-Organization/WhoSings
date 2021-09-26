package com.musixmatch.whosings.business.usecase

import com.musixmatch.whosings.data.model.Question
import com.musixmatch.whosings.data.repository.MusicRepository
import javax.inject.Inject

// Number of options provided to the user.
const val POSSIBLE_ANSWERS = 3

class QuestionsCreatorUseCase @Inject constructor(
    private val musicRepository: MusicRepository
) {

    suspend fun createQuestions(): List<Question> {
        // Get top artist from api.
        // This is just for having some artist to use as "wrong answers" in the quiz.
        val artistList = musicRepository.fetchTopArtists()

        val songList = musicRepository.getSongsWithLyrics()
        val correctAnswerIndex = (0..POSSIBLE_ANSWERS).random()

        return songList.map { song ->
            // Get first line of the lyrics.
            val firstLyricsLine = song.lyrics?.split("\n")?.first() ?: ""

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
                            (artistList.indices).random()).artistName
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

