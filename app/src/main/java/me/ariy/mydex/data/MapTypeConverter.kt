package me.ariy.mydex.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object MapTypeConverter {

    @TypeConverter
    @JvmStatic
    fun stringToMap(value: String): Map<String, String> {
        return Gson().fromJson(value,  object : TypeToken<Map<String, String>>() {}.type)
    }

    fun stringToMapInt(value: String): Map<String, Int> {
        return Gson().fromJson(value,  object : TypeToken<Map<String, Int>>() {}.type)
    }

    @TypeConverter
    @JvmStatic
    fun mapToString(value: Map<String, Int>?): String {
        return if(value == null) "" else Gson().toJson(value)
    }
}