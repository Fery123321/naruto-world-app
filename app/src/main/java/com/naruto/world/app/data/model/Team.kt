package com.naruto.world.app.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Team(
    @Json(name = "id") val id: Long,
    @Json(name = "name") val name: String,
    @Json(name = "characters") val characters: List<String>? = null
)

@JsonClass(generateAdapter = true)
data class TeamResponse(
    @Json(name = "teams") val teams: List<Team>,
    @Json(name = "currentPage") val currentPage: Int,
    @Json(name = "pageSize") val pageSize: Int,
    @Json(name = "total") val total: Int
)