package com.naruto.world.app.data.api

import com.naruto.world.app.data.model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NarutoApiService {

    // Characters
    @GET("characters")
    suspend fun getCharacters(
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("name") name: String? = null
    ): Response<CharacterResponse>

    @GET("characters/{id}")
    suspend fun getCharacterById(@Path("id") id: Long): Response<Character>

    // Clans
    @GET("clans")
    suspend fun getClans(
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null
    ): Response<ClanResponse>

    @GET("clans/{id}")
    suspend fun getClanById(@Path("id") id: Long): Response<Clan>

    // Villages
    @GET("villages")
    suspend fun getVillages(
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null
    ): Response<VillageResponse>

    @GET("villages/{id}")
    suspend fun getVillageById(@Path("id") id: Long): Response<Village>

    // Kekkei Genkai
    @GET("kekkei-genkai")
    suspend fun getKekkeiGenkai(
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null
    ): Response<KekkeiGenkaiResponse>

    // Tailed Beasts
    @GET("tailed-beasts")
    suspend fun getTailedBeasts(
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null
    ): Response<TailedBeastResponse>

    // Teams
    @GET("teams")
    suspend fun getTeams(
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null
    ): Response<TeamResponse>

    // Akatsuki
    @GET("akatsuki")
    suspend fun getAkatsuki(): Response<AkatsukiResponse>

    // Kara
    @GET("kara")
    suspend fun getKara(): Response<KaraResponse>
}