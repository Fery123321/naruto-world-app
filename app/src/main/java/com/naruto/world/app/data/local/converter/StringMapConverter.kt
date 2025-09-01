package com.naruto.world.app.data.local.converter

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class StringMapConverter {
    private val moshi = Moshi.Builder().build()
    private val mapType = Types.newParameterizedType(
        Map::class.java,
        String::class.java,
        String::class.java
    )
    private val adapter = moshi.adapter<Map<String, String>>(mapType)

    @TypeConverter
    fun fromStringMap(value: Map<String, String>?): String? {
        return value?.let { adapter.toJson(it) }
    }

    @TypeConverter
    fun toStringMap(value: String?): Map<String, String>? {
        return value?.let { adapter.fromJson(it) }
    }
}