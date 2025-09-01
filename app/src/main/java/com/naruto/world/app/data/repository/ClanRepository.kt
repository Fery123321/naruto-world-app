package com.naruto.world.app.data.repository

import android.util.Log
import com.naruto.world.app.data.api.NarutoApiService
import com.naruto.world.app.data.local.datasource.ClanLocalDataSource
import com.naruto.world.app.data.mapper.ClanMapper
import com.naruto.world.app.data.model.Clan
import com.naruto.world.app.data.model.ClanResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Response

class ClanRepository : KoinComponent {

    private val apiService: NarutoApiService by inject()
    private val localDataSource: ClanLocalDataSource by inject()

    fun getClans(page: Int? = null, limit: Int? = null): Flow<Result<ClanResponse>> = flow {
        try {
            // First try to get from local cache
            val clanCount = localDataSource.getClanCount()

            if (clanCount > 0) {
                // Return cached data first
                val cachedClans = localDataSource.getAllClans()
                cachedClans.collect { entities ->
                    val clans = ClanMapper.toModelList(entities)
                    val cachedResponse = ClanResponse(
                        clans = clans,
                        currentPage = page ?: 1,
                        pageSize = limit ?: clans.size,
                        total = clans.size
                    )
                    emit(Result.success(cachedResponse))
                }
            }

            // Then try to fetch from API to update cache
            try {
                val response = apiService.getClans(page, limit)
                if (response.isSuccessful) {
                    response.body()?.let { clanResponse ->
                        // Cache the new data
                        val entities = ClanMapper.toEntityList(clanResponse.clans)
                        localDataSource.insertClans(entities)

                        // Emit the fresh data
                        emit(Result.success(clanResponse))
                    }
                }
            } catch (apiException: Exception) {
                Log.d("ClanRepository", "API fetch failed: ${apiException.message}")
                // If API fails but we have cache, don't emit error
                if (clanCount == 0) {
                    emit(Result.failure(apiException))
                }
            }

        } catch (e: Exception) {
            Log.d("ClanRepository", "getClans error: ${e.message}")
            emit(Result.failure(e))
        }
    }

    fun getClanById(id: Long): Flow<Result<Clan>> = flow {
        try {
            // First try to get from local cache
            val cachedClan = localDataSource.getClanById(id)
            if (cachedClan != null) {
                val clan = ClanMapper.toModel(cachedClan)
                emit(Result.success(clan))
            }

            // Then try to fetch from API to update cache
            try {
                val response = apiService.getClanById(id)
                if (response.isSuccessful) {
                    response.body()?.let { clan ->
                        // Cache the new data
                        val entity = ClanMapper.toEntity(clan)
                        localDataSource.insertClan(entity)

                        // Emit the fresh data
                        emit(Result.success(clan))
                    }
                }
            } catch (apiException: Exception) {
                // If API fails but we have cache, don't emit error
                if (cachedClan == null) {
                    emit(Result.failure(apiException))
                }
            }

        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun searchClans(query: String): Flow<Result<List<Clan>>> = flow {
        try {
            // Search in local cache first
            val cachedResults = localDataSource.searchClans(query)
            cachedResults.collect { entities ->
                val clans = ClanMapper.toModelList(entities)
                emit(Result.success(clans))
            }

            // Try to search via API if needed
            try {
                val response = apiService.getClans()
                if (response.isSuccessful) {
                    response.body()?.let { clanResponse ->
                        // Cache the search results
                        val entities = ClanMapper.toEntityList(clanResponse.clans)
                        localDataSource.insertClans(entities)

                        // Emit the fresh search results
                        emit(Result.success(clanResponse.clans))
                    }
                }
            } catch (apiException: Exception) {
                // API search failed, but we already emitted cached results
                // Don't emit error to avoid disrupting the user experience
            }

        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun clearCache() {
        localDataSource.deleteAllClans()
    }

    suspend fun getCacheSize(): Int {
        return localDataSource.getClanCount()
    }
}