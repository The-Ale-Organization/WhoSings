package com.musixmatch.whosings.business.usecase

import org.junit.Test

import kotlin.random.Random

class QuestionsCreatorUseCaseTest {

    @Test
    fun createQuestions() {
        val list = List(10) {
            (0..POSSIBLE_ANSWERS).random()
        }

        val mm = List(10) {
            Random.nextInt(0, POSSIBLE_ANSWERS)
        }

        list.forEach {
            System.out.println("N = $it")
        }

        mm.forEach {
            System.out.println("MM = $it")
        }
    }
}