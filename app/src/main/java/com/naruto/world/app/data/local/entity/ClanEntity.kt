package com.naruto.world.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.naruto.world.app.data.local.converter.StringListConverter

@Entity(tableName = "clans")
@TypeConverters(StringListConverter::class)
data class ClanEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val description: String? = null,
    val characters: List<String>? = null,
    val lastUpdated: Long = System.currentTimeMillis()
)