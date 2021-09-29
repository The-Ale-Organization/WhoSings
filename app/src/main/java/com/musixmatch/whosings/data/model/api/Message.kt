package com.musixmatch.whosings.data.model.api


import com.google.gson.annotations.SerializedName

data class Message<T>(
    @SerializedName("body")
    val body: T?,
    @SerializedName("header")
    val header: Header?
)