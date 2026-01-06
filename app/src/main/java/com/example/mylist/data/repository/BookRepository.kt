package com.example.mylist.data.repository

import com.example.mylist.data.local.FavoritesManager
import com.example.mylist.data.remote.BookApi
import com.example.mylist.data.remote.BookDetailDto
import com.example.mylist.data.remote.BookWorkDto
import com.example.mylist.data.remote.RetrofitInstance
import com.example.mylist.data.remote.SearchResultDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class BookRepository(private val favoritesManager: FavoritesManager) {
    private val api = RetrofitInstance.api

    suspend fun getFictionBooks(): Result<List<BookWorkDto>> {
        return try {
            val response = api.getFictionBooks()
            Result.success(response.works)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBookDetails(workId: String): Result<BookDetailDto> {
        return try {
            val id = if (workId.startsWith("/works/")) workId.substringAfter("/works/") else workId
            val response = api.getBookDetails(id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun searchBooks(query: String): Result<List<SearchResultDto>> {
         return try {
            val response = api.searchBooks(query)
            Result.success(response.docs)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFavoriteBooks(): Result<List<SearchResultDto>> {
        return withContext(Dispatchers.IO) {
            try {
                val favorites = favoritesManager.getFavorites()
                val deferreds = favorites.map { key ->
                     async {
                         try {
                             val query = "key:$key"
                             val res = api.searchBooks(query, limit = 1)
                             res.docs.firstOrNull()
                         } catch (e: Exception) {
                             null
                         }
                     }
                }
                val results = deferreds.awaitAll().filterNotNull()
                Result.success(results)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    fun isFavorite(id: String): Boolean = favoritesManager.isFavorite(id)

    fun addFavorite(id: String) = favoritesManager.addFavorite(id)

    fun removeFavorite(id: String) = favoritesManager.removeFavorite(id)
}
