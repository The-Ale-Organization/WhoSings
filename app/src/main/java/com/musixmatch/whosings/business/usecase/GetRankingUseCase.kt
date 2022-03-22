package com.musixmatch.whosings.business.usecase

import com.musixmatch.whosings.common.data.repository.UserRepository
import javax.inject.Inject

class GetRankingUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    fun getRanking(): List<Pair<String, Int>> {
        val users = userRepository.getRegisteredUsers()
        return users?.filter { it.bestScore != null }
            ?.sortedByDescending { u -> u.bestScore }
            ?.map { Pair(it.username, it.bestScore!!) } ?: listOf()
    }

}

