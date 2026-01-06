package com.example.mylist.data.remote

import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object RetrofitInstance {
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    val api: BookApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://openlibrary.org/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(BookApi::class.java)
    }
}
