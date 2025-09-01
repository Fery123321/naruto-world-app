package com.naruto.world.app.data.repository

import com.naruto.world.app.data.api.NarutoApiService
import com.naruto.world.app.data.local.datasource.CharacterLocalDataSource
import com.naruto.world.app.data.mapper.CharacterMapper
import com.naruto.world.app.data.model.Character
import com.naruto.world.app.data.model.CharacterResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Response

class CharacterRepository : KoinComponent {

    private val apiService: NarutoApiService by inject()
    private val localDataSource: CharacterLocalDataSource by inject()

    fun getCharacters(page: Int? = null, limit: Int? = null, name: String? = null): Flow<Result<CharacterResponse>> = flow {
        try {
            // First try to get from local cache
            val characterCount = localDataSource.getCharacterCount()

            if (characterCount > 0) {
                // Return cached data first
                val cachedCharacters = localDataSource.getAllCharacters()
                cachedCharacters.collect { entities ->
                    val characters = CharacterMapper.toModelList(entities)
                    val cachedResponse = CharacterResponse(
                        characters = characters,
                        currentPage = page ?: 1,
                        pageSize = limit ?: characters.size,
                        total = characters.size
                    )
                    emit(Result.success(cachedResponse))
                }
            }

            // Then try to fetch from API to update cache
            try {
                val response = apiService.getCharacters(page, limit, name)
                if (response.isSuccessful) {
                    response.body()?.let { characterResponse ->
                        // Cache the new data
                        val entities = CharacterMapper.toEntityList(characterResponse.characters)
                        localDataSource.insertCharacters(entities)

                        // Emit the fresh data
                        emit(Result.success(characterResponse))
                    }
                }
            } catch (apiException: Exception) {
                // If API fails but we have cache, don't emit error
                if (characterCount == 0) {
                    emit(Result.failure(apiException))
                }
            }

        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun getCharacterById(id: Long): Flow<Result<Character>> = flow {
        try {
            // First try to get from local cache
            val cachedCharacter = localDataSource.getCharacterById(id)
            if (cachedCharacter != null) {
                val character = CharacterMapper.toModel(cachedCharacter)
                emit(Result.success(character))
            }

            // Then try to fetch from API to update cache
            try {
                val response = apiService.getCharacterById(id)
                if (response.isSuccessful) {
                    response.body()?.let { character ->
                        // Cache the new data
                        val entity = CharacterMapper.toEntity(character)
                        localDataSource.insertCharacter(entity)

                        // Emit the fresh data
                        emit(Result.success(character))
                    }
                }
            } catch (apiException: Exception) {
                // If API fails but we have cache, don't emit error
                if (cachedCharacter == null) {
                    emit(Result.failure(apiException))
                }
            }

        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun searchCharacters(query: String): Flow<Result<List<Character>>> = flow {
        try {
            // Search in local cache first
            val cachedResults = localDataSource.searchCharacters(query)
            cachedResults.collect { entities ->
                val characters = CharacterMapper.toModelList(entities)
                emit(Result.success(characters))
            }

            // Try to search via API if needed
            try {
                val response = apiService.getCharacters(name = query)
                if (response.isSuccessful) {
                    response.body()?.let { characterResponse ->
                        // Cache the search results
                        val entities = CharacterMapper.toEntityList(characterResponse.characters)
                        localDataSource.insertCharacters(entities)

                        // Emit the fresh search results
                        emit(Result.success(characterResponse.characters))
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
        localDataSource.deleteAllCharacters()
    }

    suspend fun getCacheSize(): Int {
        return localDataSource.getCharacterCount()
    }

    suspend fun optimizeCache() {
        try {
            // Remove stale data older than 7 days
            val sevenDaysAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000L)
            localDataSource.deleteStaleCharacters(sevenDaysAgo)

            // Keep only recent 100 characters for performance
            val characterCount = localDataSource.getCharacterCount()
            if (characterCount > MAX_CACHE_SIZE) {
                // This would require additional DAO methods for more complex cleanup
                // For now, we'll just clear all and reload popular ones
                localDataSource.deleteAllCharacters()
                preloadPopularCharacters()
            }
        } catch (e: Exception) {
            // Silent failure for cache optimization
        }
    }

    suspend fun preloadPopularCharacters() {
        try {
            // Preload first page of characters for instant access
            val response = apiService.getCharacters(page = 1, limit = PRELOAD_SIZE)
            if (response.isSuccessful) {
                response.body()?.let { characterResponse ->
                    val entities = CharacterMapper.toEntityList(characterResponse.characters)
                    localDataSource.insertCharacters(entities)
                }
            }
        } catch (e: Exception) {
            // Silent failure for preloading
        }
    }

    companion object {
        const val PRELOAD_SIZE = 50
        const val MAX_CACHE_SIZE = 200
    }
}