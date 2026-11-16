package com.example.week7.soal1.service

import com.example.week7.soal1.model.PokemonResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

object ApiConfig {
    const val API_KEY: String = "pokeprice_free_6b92b47f94d0d07990ad761659c2eb0d83ff05d294c950ac"
    const val BASE_URL: String = "https://www.pokemonpricetracker.com/"
}

interface PokemonApiService {

    @GET("api/v2/cards")
    suspend fun getCards(
        @Header("Authorization") authorization: String = "Bearer ${ApiConfig.API_KEY}",
        @Query("search") query: String = ""
    ): Response<PokemonResponse>

    companion object {
        fun create(): PokemonApiService {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            return Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PokemonApiService::class.java)
        }
    }
}
