package com.oceanbrasil.thesimpsonsgame

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personagens")
data class PersonagemEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val status: String,
    val image: String,
    val species: String
)
