package com.naruto.world.app.data.mapper

import com.naruto.world.app.data.local.entity.VillageEntity
import com.naruto.world.app.data.model.Village
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

object VillageMapper {

    private val moshi = Moshi.Builder().build()
    private val listType = Types.newParameterizedType(List::class.java, String::class.java)
    private val stringListAdapter: JsonAdapter<List<String>> = moshi.adapter(listType)

    fun toModel(entity: VillageEntity): Village {
        return Village(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            characters = entity.characters?.let { stringListAdapter.fromJson(it) }
        )
    }

    fun toModelList(entities: List<VillageEntity>): List<Village> {
        return entities.map { toModel(it) }
    }

    fun toEntity(model: Village): VillageEntity {
        return VillageEntity(
            id = model.id,
            name = model.name,
            description = model.description,
            characters = model.characters?.let { stringListAdapter.toJson(it) },
            updatedAt = System.currentTimeMillis()
        )
    }

    fun toEntityList(models: List<Village>): List<VillageEntity> {
        return models.map { toEntity(it) }
    }
}