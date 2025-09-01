package com.naruto.world.app.data.local.datasource

import com.naruto.world.app.data.local.dao.CharacterDao
import com.naruto.world.app.data.local.entity.CharacterEntity
import kotlinx.coroutines.flow.Flow

class CharacterLocalDataSource(private val characterDao: CharacterDao) {

    suspend fun insertCharacter(character: CharacterEntity) {
        characterDao.insertCharacter(character)
    }

    suspend fun insertCharacters(characters: List<CharacterEntity>) {
        characterDao.insertCharacters(characters)
    }

    suspend fun updateCharacter(character: CharacterEntity) {
        characterDao.updateCharacter(character)
    }

    suspend fun deleteCharacter(character: CharacterEntity) {
        characterDao.deleteCharacter(character)
    }

    suspend fun deleteCharacterById(characterId: Long) {
        characterDao.deleteCharacterById(characterId)
    }

    suspend fun deleteAllCharacters() {
        characterDao.deleteAllCharacters()
    }

    suspend fun getCharacterById(characterId: Long): CharacterEntity? {
        return characterDao.getCharacterById(characterId)
    }

    fun getAllCharacters(): Flow<List<CharacterEntity>> {
        return characterDao.getAllCharacters()
    }

    fun searchCharacters(query: String): Flow<List<CharacterEntity>> {
        return characterDao.searchCharacters(query)
    }

    fun getRecentCharacters(limit: Int = 20): Flow<List<CharacterEntity>> {
        return characterDao.getRecentCharacters(limit)
    }

    suspend fun getCharacterCount(): Int {
        return characterDao.getCharacterCount()
    }

    suspend fun getStaleCharacters(timestamp: Long): List<CharacterEntity> {
        return characterDao.getStaleCharacters(timestamp)
    }

    suspend fun deleteStaleCharacters(timestamp: Long): Int {
        return characterDao.deleteStaleCharacters(timestamp)
    }

    suspend fun isCacheValid(cacheTimestamp: Long, validityPeriod: Long = CACHE_VALIDITY_PERIOD): Boolean {
        val currentTime = System.currentTimeMillis()
        return (currentTime - cacheTimestamp) < validityPeriod
    }

    companion object {
        // Cache validity period: 24 hours
        const val CACHE_VALIDITY_PERIOD = 24 * 60 * 60 * 1000L
    }
}