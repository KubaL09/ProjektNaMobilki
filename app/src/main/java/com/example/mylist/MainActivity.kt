package com.example.mylist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.mylist.data.local.FavoritesManager
import com.example.mylist.data.repository.BookRepository
import com.example.mylist.ui.BookApp
import com.example.mylist.ui.theme.MyListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val favoritesManager = FavoritesManager(applicationContext)
        val repository = BookRepository(favoritesManager)

        setContent {
            MyListTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF0F2027),
                                    Color(0xFF000000)
                                )
                            )
                        )
                ) {
                    BookApp(repository)
                }
            }
        }
    }
}
