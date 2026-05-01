package com.oceanbrasil.thesimpsonsgame

import retrofit2.http.GET
import retrofit2.http.Path

interface SimpsonsApi {
    @GET("api/character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): CharacterDto
}

data class CharacterDto(
    // id, name, status, portrait_path
    val id: Int,
    val name: String,
    val status: String,
    val image: String
)