package com.naruto.world.app.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Character(
    @Json(name = "id") val id: Long,
    @Json(name = "name") val name: String,
    @Json(name = "images") val images: List<String>? = null,
    @Json(name = "debut") val debut: Debut? = null,
    @Json(name = "personal") val personal: Personal? = null,
    @Json(name = "rank") val rank: Rank? = null,
    @Json(name = "voiceActors") val voiceActors: VoiceActors? = null,
    @Json(name = "family") val family: Family? = null,
    @Json(name = "jutsu") val jutsu: List<String>? = null,
    @Json(name = "natureType") val natureType: List<String>? = null,
    @Json(name = "uniqueTraits") val uniqueTraits: List<String>? = null
)

@JsonClass(generateAdapter = true)
data class CharacterResponse(
    @Json(name = "characters") val characters: List<Character>,
    @Json(name = "currentPage") val currentPage: Int,
    @Json(name = "pageSize") val pageSize: Int,
    @Json(name = "total") val total: Int
)

@JsonClass(generateAdapter = true)
data class Debut(
    @Json(name = "manga") val manga: String? = null,
    @Json(name = "anime") val anime: String? = null,
    @Json(name = "novel") val novel: String? = null,
    @Json(name = "movie") val movie: String? = null,
    @Json(name = "game") val game: String? = null,
    @Json(name = "ova") val ova: String? = null,
    @Json(name = "appearsIn") val appearsIn: String? = null
)

@JsonClass(generateAdapter = true)
data class Personal(
    @Json(name = "birthdate") val birthdate: String? = null,
    @Json(name = "sex") val sex: String? = null,
    @Json(name = "age") val age: Map<String, String>? = null,
    @Json(name = "height") val height: Map<String, String>? = null,
    @Json(name = "weight") val weight: Map<String, String>? = null,
    @Json(name = "bloodType") val bloodType: String? = null,
    @Json(name = "kekkeiGenkai") val kekkeiGenkai: List<String>? = null,
    @Json(name = "classification") val classification: List<String>? = null,
    @Json(name = "taijutsu") val taijutsu: String? = null,
    @Json(name = "ninjutsu") val ninjutsu: String? = null,
    @Json(name = "genjutsu") val genjutsu: String? = null,
    @Json(name = "intelligence") val intelligence: String? = null,
    @Json(name = "strength") val strength: String? = null,
    @Json(name = "speed") val speed: String? = null,
    @Json(name = "stamina") val stamina: String? = null,
    @Json(name = "handSeals") val handSeals: String? = null,
    @Json(name = "status") val status: String? = null,
    @Json(name = "clan") val clan: String? = null,
    @Json(name = "occupation") val occupation: List<String>? = null,
    @Json(name = "affiliation") val affiliation: List<String>? = null,
    @Json(name = "team") val team: List<String>? = null,
    @Json(name = "partner") val partner: List<String>? = null
)

@JsonClass(generateAdapter = true)
data class Rank(
    @Json(name = "ninjaRank") val ninjaRank: Map<String, String>? = null,
    @Json(name = "ninjaRegistration") val ninjaRegistration: String? = null
)

@JsonClass(generateAdapter = true)
data class VoiceActors(
    @Json(name = "japanese") val japanese: List<String>? = null,
    @Json(name = "english") val english: List<String>? = null
)

@JsonClass(generateAdapter = true)
data class Family(
    @Json(name = "father") val father: String? = null,
    @Json(name = "mother") val mother: String? = null,
    @Json(name = "son") val son: String? = null,
    @Json(name = "daughter") val daughter: String? = null,
    @Json(name = "wife") val wife: String? = null,
    @Json(name = "husband") val husband: String? = null,
    @Json(name = "adoptiveSon") val adoptiveSon: String? = null,
    @Json(name = "adoptiveFather") val adoptiveFather: String? = null,
    @Json(name = "godfather") val godfather: String? = null,
    @Json(name = "grandfather") val grandfather: String? = null,
    @Json(name = "grandmother") val grandmother: String? = null,
    @Json(name = "brother") val brother: String? = null,
    @Json(name = "sister") val sister: String? = null,
    @Json(name = "uncle") val uncle: String? = null,
    @Json(name = "aunt") val aunt: String? = null,
    @Json(name = "cousin") val cousin: String? = null,
    @Json(name = "nephew") val nephew: String? = null,
    @Json(name = "niece") val niece: String? = null,
    @Json(name = "grandson") val grandson: String? = null,
    @Json(name = "granddaughter") val granddaughter: String? = null,
    @Json(name = "pet") val pet: String? = null,
    @Json(name = "relative") val relative: List<String>? = null,
    @Json(name = "incarnationWithTheGodTree") val incarnationWithTheGodTree: String? = null
)