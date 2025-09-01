package com.naruto.world.app.data.local.dao

import androidx.room.*
import com.naruto.world.app.data.local.entity.VillageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VillageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVillage(village: VillageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVillages(villages: List<VillageEntity>)

    @Query("SELECT * FROM villages ORDER BY name ASC")
    fun getAllVillages(): Flow<List<VillageEntity>>

    @Query("SELECT * FROM villages WHERE id = :id")
    suspend fun getVillageById(id: Long): VillageEntity?

    @Query("SELECT * FROM villages ORDER BY name ASC LIMIT :limit OFFSET :offset")
    fun getVillagesPaged(limit: Int, offset: Int): Flow<List<VillageEntity>>

    @Query("SELECT COUNT(*) FROM villages")
    suspend fun getVillageCount(): Int

    @Query("SELECT * FROM villages WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchVillages(query: String): Flow<List<VillageEntity>>

    @Query("DELETE FROM villages WHERE id = :id")
    suspend fun deleteVillageById(id: Long)

    @Query("DELETE FROM villages")
    suspend fun deleteAllVillages()

    @Query("DELETE FROM villages WHERE updatedAt < :timestamp")
    suspend fun deleteStaleVillages(timestamp: Long)

    @Update
    suspend fun updateVillage(village: VillageEntity)
}