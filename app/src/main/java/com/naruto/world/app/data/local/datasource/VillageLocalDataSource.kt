package com.naruto.world.app.data.local.datasource

import com.naruto.world.app.data.local.dao.VillageDao
import com.naruto.world.app.data.local.entity.VillageEntity
import kotlinx.coroutines.flow.Flow

class VillageLocalDataSource(private val villageDao: VillageDao) {

    suspend fun insertVillage(village: VillageEntity) {
        villageDao.insertVillage(village)
    }

    suspend fun insertVillages(villages: List<VillageEntity>) {
        villageDao.insertVillages(villages)
    }

    fun getAllVillages(): Flow<List<VillageEntity>> {
        return villageDao.getAllVillages()
    }

    suspend fun getVillageById(id: Long): VillageEntity? {
        return villageDao.getVillageById(id)
    }

    fun getVillagesPaged(limit: Int, offset: Int): Flow<List<VillageEntity>> {
        return villageDao.getVillagesPaged(limit, offset)
    }

    suspend fun getVillageCount(): Int {
        return villageDao.getVillageCount()
    }

    fun searchVillages(query: String): Flow<List<VillageEntity>> {
        return villageDao.searchVillages(query)
    }

    suspend fun deleteVillageById(id: Long) {
        villageDao.deleteVillageById(id)
    }

    suspend fun deleteAllVillages() {
        villageDao.deleteAllVillages()
    }

    suspend fun deleteStaleVillages(timestamp: Long) {
        villageDao.deleteStaleVillages(timestamp)
    }

    suspend fun updateVillage(village: VillageEntity) {
        villageDao.updateVillage(village)
    }
}