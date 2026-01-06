package com.example.mylist.data.local

import android.content.Context
import android.content.SharedPreferences

class FavoritesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("book_favorites", Context.MODE_PRIVATE)

    fun addFavorite(id: String) {
        val current = getFavorites().toMutableSet()
        current.add(id)
        prefs.edit().putStringSet("favorites_ids", current).apply()
    }

    fun removeFavorite(id: String) {
        val current = getFavorites().toMutableSet()
        current.remove(id)
        prefs.edit().putStringSet("favorites_ids", current).apply()
    }

    fun isFavorite(id: String): Boolean {
        return getFavorites().contains(id)
    }

    fun getFavorites(): Set<String> {
        return prefs.getStringSet("favorites_ids", emptySet()) ?: emptySet()
    }
}
