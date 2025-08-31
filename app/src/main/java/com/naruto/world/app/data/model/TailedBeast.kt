package com.naruto.world.app.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TailedBeast(
    @Json(name = "id") val id: Long,
    @Json(name = "name") val name: String,
    @Json(name = "description") val description: String? = null,
    @Json(name = "images") val images: List<String>? = null,
    @Json(name = "jinchuriki") val jinchuriki: List<String>? = null
)

@JsonClass(generateAdapter = true)
data class TailedBeastResponse(
    @Json(name = "tailedBeasts") val tailedBeasts: List<TailedBeast>,
    @Json(name = "currentPage") val currentPage: Int,
    @Json(name = "pageSize") val pageSize: Int,
    @Json(name = "total") val total: Int
)