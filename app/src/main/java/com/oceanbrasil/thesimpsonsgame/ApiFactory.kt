package com.oceanbrasil.thesimpsonsgame

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiFactory {
    val api = Retrofit.Builder()
        .baseUrl("https://thesimpsonsapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SimpsonsApi::class.java)
}