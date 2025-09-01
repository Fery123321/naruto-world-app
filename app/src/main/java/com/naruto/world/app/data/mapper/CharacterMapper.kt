package com.naruto.world.app.data.mapper

import com.naruto.world.app.data.local.entity.CharacterEntity
import com.naruto.world.app.data.model.Character

object CharacterMapper {

    fun toEntity(character: Character): CharacterEntity {
        return CharacterEntity(
            id = character.id,
            name = character.name,
            images = character.images,
            debutManga = character.debut?.manga,
            debutAnime = character.debut?.anime,
            debutNovel = character.debut?.novel,
            debutMovie = character.debut?.movie,
            debutGame = character.debut?.game,
            debutOva = character.debut?.ova,
            birthdate = character.personal?.birthdate,
            sex = character.personal?.sex,
            age = character.personal?.age,
            height = character.personal?.height,
            weight = character.personal?.weight,
            bloodType = character.personal?.bloodType,
            kekkeiGenkai = character.personal?.kekkeiGenkai,
            classification = character.personal?.classification,
            taijutsu = character.personal?.taijutsu,
            ninjutsu = character.personal?.ninjutsu,
            genjutsu = character.personal?.genjutsu,
            intelligence = character.personal?.intelligence,
            strength = character.personal?.strength,
            speed = character.personal?.speed,
            stamina = character.personal?.stamina,
            handSeals = character.personal?.handSeals,
            status = character.personal?.status,
            clan = character.personal?.clan,
            occupation = character.personal?.occupation,
            affiliation = character.personal?.affiliation,
            team = character.personal?.team,
            partner = character.personal?.partner,
            ninjaRank = character.rank?.ninjaRank,
            ninjaRegistration = character.rank?.ninjaRegistration,
            jutsu = character.jutsu,
            natureType = character.natureType,
            uniqueTraits = character.uniqueTraits,
            lastUpdated = System.currentTimeMillis()
        )
    }

    fun toModel(entity: CharacterEntity): Character {
        return Character(
            id = entity.id,
            name = entity.name,
            images = entity.images,
            debut = entity.debutManga?.let { manga ->
                com.naruto.world.app.data.model.Debut(
                    manga = manga,
                    anime = entity.debutAnime,
                    novel = entity.debutNovel,
                    movie = entity.debutMovie,
                    game = entity.debutGame,
                    ova = entity.debutOva
                )
            },
            personal = com.naruto.world.app.data.model.Personal(
                birthdate = entity.birthdate,
                sex = entity.sex,
                age = entity.age,
                height = entity.height,
                weight = entity.weight,
                bloodType = entity.bloodType,
                kekkeiGenkai = entity.kekkeiGenkai,
                classification = entity.classification,
                taijutsu = entity.taijutsu,
                ninjutsu = entity.ninjutsu,
                genjutsu = entity.genjutsu,
                intelligence = entity.intelligence,
                strength = entity.strength,
                speed = entity.speed,
                stamina = entity.stamina,
                handSeals = entity.handSeals,
                status = entity.status,
                clan = entity.clan,
                occupation = entity.occupation,
                affiliation = entity.affiliation,
                team = entity.team,
                partner = entity.partner
            ),
            rank = entity.ninjaRank?.let { rank ->
                com.naruto.world.app.data.model.Rank(
                    ninjaRank = rank,
                    ninjaRegistration = entity.ninjaRegistration
                )
            },
            voiceActors = null, // Not stored in local database
            family = null, // Not stored in local database
            jutsu = entity.jutsu,
            natureType = entity.natureType,
            uniqueTraits = entity.uniqueTraits
        )
    }

    fun toEntityList(characters: List<Character>): List<CharacterEntity> {
        return characters.map { toEntity(it) }
    }

    fun toModelList(entities: List<CharacterEntity>): List<Character> {
        return entities.map { toModel(it) }
    }
}