package com.oceanbrasil.thesimpsonsgame

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personagens")
data class PersonagemEntity(
    @PrimaryKey(autoGenerate = true) val localId: Int = 0,
    val remoteId: Int,
    val name: String,
    val status: String,
    val image: String,
    val species: String,
    val series: String // "simpsons" ou "rickandmorty"
)
