package com.naruto.world.app.data.repository

import android.util.Log
import com.naruto.world.app.data.api.NarutoApiService
import com.naruto.world.app.data.local.datasource.VillageLocalDataSource
import com.naruto.world.app.data.mapper.VillageMapper
import com.naruto.world.app.data.model.Village
import com.naruto.world.app.data.model.VillageResponse
import com.naruto.world.app.utils.PerformanceMonitor
import com.naruto.world.app.utils.measureTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Response

class VillageRepository : KoinComponent {

    private val apiService: NarutoApiService by inject()
    private val localDataSource: VillageLocalDataSource by inject()

    fun getVillages(page: Int? = null, limit: Int? = null): Flow<Result<VillageResponse>> = flow {
        try {
            // First try to get from local cache
            val villageCount = measureTime("getVillageCount") {
                localDataSource.getVillageCount()
            }

            if (villageCount > 0) {
                // Return cached data first
                val cachedVillages = measureTime("getAllVillages") {
                    localDataSource.getAllVillages()
                }
                cachedVillages.collect { entities ->
                    val villages = measureTime("VillageMapper.toModelList") {
                        VillageMapper.toModelList(entities)
                    }
                    val cachedResponse = VillageResponse(
                        villages = villages,
                        currentPage = page ?: 1,
                        pageSize = limit ?: villages.size,
                        total = villages.size
                    )
                    PerformanceMonitor.logDatabaseOperation("getVillages_cache", villages.size, 0)
                    emit(Result.success(cachedResponse))
                }
            }

            // Then try to fetch from API to update cache
            try {
                val response = measureTime("apiService.getVillages") {
                    apiService.getVillages(page, limit)
                }
                if (response.isSuccessful) {
                    response.body()?.let { villageResponse ->
                        // Cache the new data
                        val entities = measureTime("VillageMapper.toEntityList") {
                            VillageMapper.toEntityList(villageResponse.villages)
                        }
                        measureTime("insertVillages") {
                            localDataSource.insertVillages(entities)
                        }

                        PerformanceMonitor.logDatabaseOperation("insertVillages", villageResponse.villages.size, 0)
                        // Emit the fresh data
                        emit(Result.success(villageResponse))
                    }
                }
            } catch (apiException: Exception) {
                Log.d("VillageRepository", "API fetch failed: ${apiException.message}")
                // If API fails but we have cache, don't emit error
                if (villageCount == 0) {
                    emit(Result.failure(apiException))
                }
            }

        } catch (e: Exception) {
            Log.d("VillageRepository", "getVillages error: ${e.message}")
            emit(Result.failure(e))
        }
    }

    fun getVillageById(id: Long): Flow<Result<Village>> = flow {
        try {
            // First try to get from local cache
            val cachedVillage = localDataSource.getVillageById(id)
            if (cachedVillage != null) {
                val village = VillageMapper.toModel(cachedVillage)
                emit(Result.success(village))
            }

            // Then try to fetch from API to update cache
            try {
                val response = apiService.getVillageById(id)
                if (response.isSuccessful) {
                    response.body()?.let { village ->
                        // Cache the new data
                        val entity = VillageMapper.toEntity(village)
                        localDataSource.insertVillage(entity)

                        // Emit the fresh data
                        emit(Result.success(village))
                    }
                }
            } catch (apiException: Exception) {
                // If API fails but we have cache, don't emit error
                if (cachedVillage == null) {
                    emit(Result.failure(apiException))
                }
            }

        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun searchVillages(query: String): Flow<Result<List<Village>>> = flow {
        try {
            // Search in local cache first
            val cachedResults = localDataSource.searchVillages(query)
            cachedResults.collect { entities ->
                val villages = VillageMapper.toModelList(entities)
                emit(Result.success(villages))
            }

            // Try to search via API if needed
            try {
                val response = apiService.getVillages()
                if (response.isSuccessful) {
                    response.body()?.let { villageResponse ->
                        // Cache the search results
                        val entities = VillageMapper.toEntityList(villageResponse.villages)
                        localDataSource.insertVillages(entities)

                        // Emit the fresh search results
                        emit(Result.success(villageResponse.villages))
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
        localDataSource.deleteAllVillages()
    }

    suspend fun getCacheSize(): Int {
        return localDataSource.getVillageCount()
    }

    fun getVillagesPaged(page: Int, pageSize: Int = DEFAULT_PAGE_SIZE): Flow<Result<VillageResponse>> = flow {
        try {
            val offset = page * pageSize

            // First try to get from local cache
            val cachedVillages = localDataSource.getVillagesPaged(pageSize, offset)
            cachedVillages.collect { entities ->
                if (entities.isNotEmpty()) {
                    val villages = VillageMapper.toModelList(entities)
                    val cachedResponse = VillageResponse(
                        villages = villages,
                        currentPage = page,
                        pageSize = pageSize,
                        total = localDataSource.getVillageCount()
                    )
                    emit(Result.success(cachedResponse))
                }
            }

            // Then try to fetch from API to update cache
            try {
                val apiPage = page + 1 // API uses 1-based pagination
                val response = apiService.getVillages(page = apiPage, limit = pageSize)
                if (response.isSuccessful) {
                    response.body()?.let { villageResponse ->
                        // Cache the new data
                        val entities = VillageMapper.toEntityList(villageResponse.villages)
                        localDataSource.insertVillages(entities)

                        // Emit the fresh data
                        emit(Result.success(villageResponse))
                    }
                }
            } catch (apiException: Exception) {
                // If API fails but we have cache, don't emit error
                // The cached data was already emitted above
            }

        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun preloadPopularVillages() {
        try {
            // Preload first page of villages for instant access
            val response = apiService.getVillages(page = 1, limit = PRELOAD_SIZE)
            if (response.isSuccessful) {
                response.body()?.let { villageResponse ->
                    val entities = VillageMapper.toEntityList(villageResponse.villages)
                    localDataSource.insertVillages(entities)
                }
            }
        } catch (e: Exception) {
            // Silent failure for preloading
        }
    }

    suspend fun optimizeCache() {
        try {
            // Remove stale data older than 7 days
            val sevenDaysAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000L)
            localDataSource.deleteStaleVillages(sevenDaysAgo)

            // Keep only recent 100 villages for performance
            val villageCount = localDataSource.getVillageCount()
            if (villageCount > MAX_CACHE_SIZE) {
                // This would require additional DAO methods for more complex cleanup
                // For now, we'll just clear all and reload popular ones
                localDataSource.deleteAllVillages()
                preloadPopularVillages()
            }
        } catch (e: Exception) {
            // Silent failure for cache optimization
        }
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 20
        const val PRELOAD_SIZE = 50
        const val MAX_CACHE_SIZE = 200
    }
}