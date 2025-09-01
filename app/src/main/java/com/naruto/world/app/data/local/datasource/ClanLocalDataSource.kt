package com.naruto.world.app.data.local.datasource

import com.naruto.world.app.data.local.dao.ClanDao
import com.naruto.world.app.data.local.entity.ClanEntity
import kotlinx.coroutines.flow.Flow

class ClanLocalDataSource(private val clanDao: ClanDao) {

    suspend fun insertClan(clan: ClanEntity) {
        clanDao.insertClan(clan)
    }

    suspend fun insertClans(clans: List<ClanEntity>) {
        clanDao.insertClans(clans)
    }

    suspend fun updateClan(clan: ClanEntity) {
        clanDao.updateClan(clan)
    }

    suspend fun deleteClan(clan: ClanEntity) {
        clanDao.deleteClan(clan)
    }

    suspend fun deleteClanById(clanId: Long) {
        clanDao.deleteClanById(clanId)
    }

    suspend fun deleteAllClans() {
        clanDao.deleteAllClans()
    }

    suspend fun getClanById(clanId: Long): ClanEntity? {
        return clanDao.getClanById(clanId)
    }

    fun getAllClans(): Flow<List<ClanEntity>> {
        return clanDao.getAllClans()
    }

    fun searchClans(query: String): Flow<List<ClanEntity>> {
        return clanDao.searchClans(query)
    }

    fun getRecentClans(limit: Int = 20): Flow<List<ClanEntity>> {
        return clanDao.getRecentClans(limit)
    }

    suspend fun getClanCount(): Int {
        return clanDao.getClanCount()
    }

    suspend fun getStaleClans(timestamp: Long): List<ClanEntity> {
        return clanDao.getStaleClans(timestamp)
    }

    suspend fun deleteStaleClans(timestamp: Long): Int {
        return clanDao.deleteStaleClans(timestamp)
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