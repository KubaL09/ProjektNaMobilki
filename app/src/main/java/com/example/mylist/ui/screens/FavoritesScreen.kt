package com.example.mylist.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.mylist.ui.components.BookListItem
import com.example.mylist.ui.components.ErrorItem
import com.example.mylist.ui.components.LoadingView
import com.example.mylist.ui.viewmodel.FavoritesUiState
import com.example.mylist.ui.viewmodel.FavoritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    onBack: () -> Unit,
    onBookClick: (String, String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadFavorites()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ulubione") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Wstecz")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (val state = uiState) {
                is FavoritesUiState.Loading -> LoadingView()
                is FavoritesUiState.Error -> ErrorItem(state.message) { viewModel.loadFavorites() }
                is FavoritesUiState.Empty -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Brak ulubionych książek")
                    }
                }
                is FavoritesUiState.Success -> {
                    LazyColumn {
                        items(state.favorites) { book ->
                            val authorName = book.authorName?.firstOrNull() ?: "Nieznany autor"
                            BookListItem(
                                title = book.title,
                                author = authorName,
                                coverId = book.coverId,
                                onClick = {
                                    onBookClick(book.key, authorName)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
