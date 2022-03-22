package com.musixmatch.whosings.common.data.model.api


import com.google.gson.annotations.SerializedName

data class Header(
    @SerializedName("available")
    val available: Int?,
    @SerializedName("execute_time")
    val executeTime: Double?,
    @SerializedName("status_code")
    val statusCode: Int?
)