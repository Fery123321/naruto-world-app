package com.naruto.world.app.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// Akatsuki
@JsonClass(generateAdapter = true)
data class AkatsukiMember(
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
data class AkatsukiResponse(
    @Json(name = "akatsuki") val akatsuki: List<AkatsukiMember>
)

// Kara
@JsonClass(generateAdapter = true)
data class KaraMember(
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
data class KaraResponse(
    @Json(name = "kara") val kara: List<KaraMember>
)