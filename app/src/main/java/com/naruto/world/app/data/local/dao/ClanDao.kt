package com.naruto.world.app.data.local.dao

import androidx.room.*
import com.naruto.world.app.data.local.entity.ClanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClan(clan: ClanEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClans(clans: List<ClanEntity>)

    @Update
    suspend fun updateClan(clan: ClanEntity)

    @Delete
    suspend fun deleteClan(clan: ClanEntity)

    @Query("DELETE FROM clans WHERE id = :clanId")
    suspend fun deleteClanById(clanId: Long)

    @Query("DELETE FROM clans")
    suspend fun deleteAllClans()

    @Query("SELECT * FROM clans WHERE id = :clanId")
    suspend fun getClanById(clanId: Long): ClanEntity?

    @Query("SELECT * FROM clans ORDER BY name ASC")
    fun getAllClans(): Flow<List<ClanEntity>>

    @Query("SELECT * FROM clans WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchClans(query: String): Flow<List<ClanEntity>>

    @Query("SELECT * FROM clans ORDER BY lastUpdated DESC LIMIT :limit")
    fun getRecentClans(limit: Int = 20): Flow<List<ClanEntity>>

    @Query("SELECT COUNT(*) FROM clans")
    suspend fun getClanCount(): Int

    @Query("SELECT * FROM clans WHERE lastUpdated < :timestamp")
    suspend fun getStaleClans(timestamp: Long): List<ClanEntity>

    @Query("DELETE FROM clans WHERE lastUpdated < :timestamp")
    suspend fun deleteStaleClans(timestamp: Long): Int
}