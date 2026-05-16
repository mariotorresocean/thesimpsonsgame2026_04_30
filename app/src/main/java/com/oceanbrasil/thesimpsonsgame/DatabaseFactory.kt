package com.oceanbrasil.thesimpsonsgame

import android.content.Context
import androidx.room.Room.databaseBuilder

object DatabaseFactory {
    private  var instance: AppDatabase? = null
    fun get(context: Context): AppDatabase {
        return instance ?: synchronized(this) {
            databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "rickandmorty.db"
            ).build().also { instance = it }
        }
    }
}