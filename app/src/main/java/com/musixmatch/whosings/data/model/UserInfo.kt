package com.musixmatch.whosings.data.model

data class UserInfo(
    val username: String,
    val bestScore: Int?,
    val rankingPosition: Int?,
    val totalUsers: Int?,
    val avatarUrl: String
)
