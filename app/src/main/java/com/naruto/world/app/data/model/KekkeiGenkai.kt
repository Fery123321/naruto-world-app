package com.naruto.world.app.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KekkeiGenkai(
    @Json(name = "id") val id: Long,
    @Json(name = "name") val name: String,
    @Json(name = "description") val description: String? = null,
    @Json(name = "characters") val characters: List<String>? = null
)

@JsonClass(generateAdapter = true)
data class KekkeiGenkaiResponse(
    @Json(name = "kekkeiGenkai") val kekkeiGenkai: List<KekkeiGenkai>,
    @Json(name = "currentPage") val currentPage: Int,
    @Json(name = "pageSize") val pageSize: Int,
    @Json(name = "total") val total: Int
)