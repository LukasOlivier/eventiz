package com.example.eventiz.network

import com.example.eventiz.model.AnnouncementResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET

private const val BASE_URL =
    "https://eventiz-strapi.lukasolivier.be/api/"


private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface EventizCmsService {
    @GET("announcements?populate=category")
    suspend fun getAnnouncements(): AnnouncementResponse
}

object EventizCms {
    val retrofitService : EventizCmsService by lazy {
        retrofit.create(EventizCmsService::class.java)
    }
}


