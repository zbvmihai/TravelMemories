package com.zabi.travelmemories.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zabi.travelmemories.models.Location

class Convertors {

    @TypeConverter
    fun fromLocation(location: Location): String {
        return Gson().toJson(location)
    }

    @TypeConverter
    fun toLocation(locationString: String): Location {
        val type = object: TypeToken<Location>() {}.type
        return Gson().fromJson(locationString, type)
    }
}