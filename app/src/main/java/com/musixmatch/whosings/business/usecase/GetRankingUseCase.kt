package com.musixmatch.whosings.business.usecase

import com.musixmatch.whosings.data.repository.UserRepository
import javax.inject.Inject

class GetRankingUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    fun getRanking(): List<Pair<String, Int>> {
        val users = userRepository.getRegisteredUsers()
        return userRepository.getRegisteredUsers()?.filter { it.bestScore != null }
            ?.sortedBy { u -> u.bestScore }
            ?.map { Pair(it.username, it.bestScore!!) } ?: listOf()
    }

}

