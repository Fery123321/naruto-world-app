package com.naruto.world.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "villages")
data class VillageEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val description: String? = null,
    val characters: String? = null, // JSON string of character names
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)