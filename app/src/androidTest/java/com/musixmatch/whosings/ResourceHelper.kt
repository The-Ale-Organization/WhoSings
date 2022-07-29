package com.musixmatch.whosings

import android.content.Context

object ResourceHelper {

    fun loadString(context: Context, fileName: String): String {
        return context.assets.open(fileName)
            .bufferedReader()
            .use { it.readText() }

    }

}