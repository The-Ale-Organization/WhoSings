package com.musixmatch.whosings.data.model


import com.google.gson.annotations.SerializedName

data class Response<T>(
    @SerializedName("message")
    val message: Message<T>?
)