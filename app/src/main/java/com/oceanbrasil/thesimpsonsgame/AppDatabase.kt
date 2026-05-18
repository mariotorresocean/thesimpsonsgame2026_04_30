package com.oceanbrasil.thesimpsonsgame

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PersonagemEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun personagemDao(): PersonagemDao
}