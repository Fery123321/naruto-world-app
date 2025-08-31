package com.naruto.world.app.data.repository

import com.naruto.world.app.data.api.NarutoApiService
import com.naruto.world.app.data.model.Character
import com.naruto.world.app.data.model.CharacterResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Response

class CharacterRepository : KoinComponent {

    private val apiService: NarutoApiService by inject()

    fun getCharacters(page: Int? = null, limit: Int? = null, name: String? = null): Flow<Result<CharacterResponse>> = flow {
        try {
            val response = apiService.getCharacters(page, limit, name)
            if (response.isSuccessful) {
                response.body()?.let { characterResponse ->
                    emit(Result.success(characterResponse))
                } ?: emit(Result.failure(Exception("Empty response")))
            } else {
                emit(Result.failure(Exception("API Error: ${response.code()} ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun getCharacterById(id: Long): Flow<Result<Character>> = flow {
        try {
            val response = apiService.getCharacterById(id)
            if (response.isSuccessful) {
                response.body()?.let { character ->
                    emit(Result.success(character))
                } ?: emit(Result.failure(Exception("Character not found")))
            } else {
                emit(Result.failure(Exception("API Error: ${response.code()} ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}