package com.example.mylist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mylist.data.local.FavoritesManager
import com.example.mylist.data.repository.BookRepository
import com.example.mylist.ui.BookApp
import com.example.mylist.ui.theme.MyListTheme // Assuming theme exists

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val favoritesManager = FavoritesManager(applicationContext)
        val repository = BookRepository(favoritesManager)

        setContent {
            androidx.compose.material3.MaterialTheme {
                BookApp(repository)
            }
        }
    }
}
