package com.example.eventiz.network

import com.example.eventiz.model.Emergency
import com.example.eventiz.model.Sensor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

private const val BASE_URL =
    "https://eventiz-laravel.lukasolivier.be/api/"


private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface EventizApiService {
    @POST("emergency")
    suspend fun postEmergency(@Body dataModel: Emergency?)
    @GET("sensors")
    suspend fun getSensors(): List<Sensor>
}

object EventizApi {
    val retrofitService : EventizApiService by lazy {
        retrofit.create(EventizApiService::class.java)
    }
}


