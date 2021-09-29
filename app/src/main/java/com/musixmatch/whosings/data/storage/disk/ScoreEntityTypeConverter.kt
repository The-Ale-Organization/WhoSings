package com.musixmatch.whosings.data.storage.disk

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.musixmatch.whosings.data.model.entity.ScoreEntity
import java.io.Serializable
import java.lang.reflect.Type

class ScoreEntityTypeConverter : Serializable {

    @TypeConverter
    fun fromListToString(scores: List<ScoreEntity>?): String? {
        if (scores == null) {
            return null
        }
        val gson = Gson()
        val type: Type =
            object : TypeToken<List<ScoreEntity>?>() {}.type
        return gson.toJson(scores, type)
    }

    @TypeConverter
    fun fromStringToList(scoresString: String?): List<ScoreEntity>? {
        if (scoresString == null) {
            return null
        }
        val gson = Gson()
        val type: Type =
            object : TypeToken<List<ScoreEntity>?>() {}.type
        return gson.fromJson<List<ScoreEntity>>(scoresString, type)
    }

}
