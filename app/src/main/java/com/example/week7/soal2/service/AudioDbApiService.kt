package com.example.week7.soal2.service

import com.example.week7.soal2.model.AlbumResponse
import com.example.week7.soal2.model.ArtistResponse
import com.example.week7.soal2.model.TrackResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object AudioDbConfig {
    const val BASE_URL: String = "https://www.theaudiodb.com/api/v1/json/2/"
}

/**
 * Service Retrofit untuk TheAudioDB API.
 */
interface AudioDbApiService {

    @GET("search.php")
    suspend fun searchArtist(
        @Query("s") artistName: String
    ): Response<ArtistResponse>

    @GET("searchalbum.php")
    suspend fun searchAlbums(
        @Query("s") artistName: String
    ): Response<AlbumResponse>

    @GET("album.php")
    suspend fun getAlbumDetail(
        @Query("m") albumId: String
    ): Response<AlbumResponse>

    @GET("track.php")
    suspend fun getTracks(
        @Query("m") albumId: String
    ): Response<TrackResponse>

    companion object {
        fun create(): AudioDbApiService {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            return Retrofit.Builder()
                .baseUrl(AudioDbConfig.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AudioDbApiService::class.java)
        }
    }
}
