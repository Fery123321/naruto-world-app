package com.naruto.world.app.data.local.dao

import androidx.room.*
import com.naruto.world.app.data.local.entity.CharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: CharacterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterEntity>)

    @Update
    suspend fun updateCharacter(character: CharacterEntity)

    @Delete
    suspend fun deleteCharacter(character: CharacterEntity)

    @Query("DELETE FROM characters WHERE id = :characterId")
    suspend fun deleteCharacterById(characterId: Long)

    @Query("DELETE FROM characters")
    suspend fun deleteAllCharacters()

    @Query("SELECT * FROM characters WHERE id = :characterId")
    suspend fun getCharacterById(characterId: Long): CharacterEntity?

    @Query("SELECT * FROM characters ORDER BY name ASC")
    fun getAllCharacters(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characters WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchCharacters(query: String): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characters ORDER BY lastUpdated DESC LIMIT :limit")
    fun getRecentCharacters(limit: Int = 20): Flow<List<CharacterEntity>>

    @Query("SELECT COUNT(*) FROM characters")
    suspend fun getCharacterCount(): Int

    @Query("SELECT * FROM characters WHERE lastUpdated < :timestamp")
    suspend fun getStaleCharacters(timestamp: Long): List<CharacterEntity>

    @Query("DELETE FROM characters WHERE lastUpdated < :timestamp")
    suspend fun deleteStaleCharacters(timestamp: Long): Int
}