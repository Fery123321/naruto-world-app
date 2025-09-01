package com.naruto.world.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.naruto.world.app.data.local.converter.StringListConverter
import com.naruto.world.app.data.local.converter.StringMapConverter

@Entity(tableName = "characters")
@TypeConverters(StringListConverter::class, StringMapConverter::class)
data class CharacterEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val images: List<String>? = null,
    val debutManga: String? = null,
    val debutAnime: String? = null,
    val debutNovel: String? = null,
    val debutMovie: String? = null,
    val debutGame: String? = null,
    val debutOva: String? = null,
    val birthdate: String? = null,
    val sex: String? = null,
    val age: Map<String, String>? = null,
    val height: Map<String, String>? = null,
    val weight: Map<String, String>? = null,
    val bloodType: String? = null,
    val kekkeiGenkai: List<String>? = null,
    val classification: List<String>? = null,
    val taijutsu: String? = null,
    val ninjutsu: String? = null,
    val genjutsu: String? = null,
    val intelligence: String? = null,
    val strength: String? = null,
    val speed: String? = null,
    val stamina: String? = null,
    val handSeals: String? = null,
    val status: String? = null,
    val clan: String? = null,
    val occupation: List<String>? = null,
    val affiliation: List<String>? = null,
    val team: List<String>? = null,
    val partner: List<String>? = null,
    val ninjaRank: Map<String, String>? = null,
    val ninjaRegistration: String? = null,
    val jutsu: List<String>? = null,
    val natureType: List<String>? = null,
    val uniqueTraits: List<String>? = null,
    val lastUpdated: Long = System.currentTimeMillis()
)