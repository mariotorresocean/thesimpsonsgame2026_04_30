package com.oceanbrasil.thesimpsonsgame

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonagemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirTodos(personagens: List<PersonagemEntity>)

    @Query("SELECT * FROM personagens WHERE series = :series ORDER BY name ASC")
    fun observarPorSerie(series: String): Flow<List<PersonagemEntity>>

    @Query("SELECT * FROM personagens ORDER BY name ASC")
    fun observarTodos(): Flow<List<PersonagemEntity>>
}