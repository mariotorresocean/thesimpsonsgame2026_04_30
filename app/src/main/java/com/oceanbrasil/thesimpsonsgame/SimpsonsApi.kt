package com.oceanbrasil.thesimpsonsgame

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SimpsonsApi {
    @GET("api/character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): CharacterDto
    @GET("api/character")
    suspend fun getCharacters(@Query("page") page: Int): CharactersResponse
}

data class CharactersResponse(
    val info: PageInfo,
    val results: List<CharacterDto>
)

data class PageInfo(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)

data class CharacterDto(
    // id, name, status, portrait_path
    val id: Int,
    val name: String,
    val status: String,
    val image: String,
    val species: String
)