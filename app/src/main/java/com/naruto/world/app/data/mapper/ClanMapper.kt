package com.naruto.world.app.data.mapper

import com.naruto.world.app.data.local.entity.ClanEntity
import com.naruto.world.app.data.model.Clan

object ClanMapper {

    fun toEntity(clan: Clan): ClanEntity {
        return ClanEntity(
            id = clan.id,
            name = clan.name,
            description = clan.description,
            characters = clan.characters,
            lastUpdated = System.currentTimeMillis()
        )
    }

    fun toModel(entity: ClanEntity): Clan {
        return Clan(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            characters = entity.characters
        )
    }

    fun toEntityList(clans: List<Clan>): List<ClanEntity> {
        return clans.map { toEntity(it) }
    }

    fun toModelList(entities: List<ClanEntity>): List<Clan> {
        return entities.map { toModel(it) }
    }
}