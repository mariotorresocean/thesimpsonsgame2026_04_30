package com.oceanbrasil.thesimpsonsgame

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiFactory {
    private fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val rickAndMortyApi = createRetrofit("https://rickandmortyapi.com/").create(SimpsonsApi::class.java)
    val simpsonsApi = createRetrofit("https://api.sampleapis.com/simpsons/").create(SimpsonsApi::class.java)
}
