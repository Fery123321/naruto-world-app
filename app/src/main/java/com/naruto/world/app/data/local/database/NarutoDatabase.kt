package com.naruto.world.app.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.naruto.world.app.data.local.converter.StringListConverter
import com.naruto.world.app.data.local.converter.StringMapConverter
import com.naruto.world.app.data.local.dao.CharacterDao
import com.naruto.world.app.data.local.entity.CharacterEntity

@Database(
    entities = [CharacterEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(StringListConverter::class, StringMapConverter::class)
abstract class NarutoDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao

    companion object {
        const val DATABASE_NAME = "naruto_database"
    }
}