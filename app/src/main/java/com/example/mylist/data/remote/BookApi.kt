package com.example.mylist.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookApi {
    @GET("subjects/fiction.json")
    suspend fun getFictionBooks(@Query("limit") limit: Int = 20): BookListResponse

    @GET("works/{workId}.json")
    suspend fun getBookDetails(@Path("workId") workId: String): BookDetailDto

    @GET("search.json")
    suspend fun searchBooks(@Query("q") query: String, @Query("limit") limit: Int = 20): SearchResponseDto
}
