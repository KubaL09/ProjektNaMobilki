package com.example.mylist.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.mylist.ui.components.BookListItem
import com.example.mylist.ui.components.ErrorItem
import com.example.mylist.ui.components.LoadingView
import com.example.mylist.ui.viewmodel.HomeUiState
import com.example.mylist.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onBookClick: (String, String) -> Unit,
    onFavoritesClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = androidx.compose.ui.graphics.Color.Transparent,
        topBar = {
            TopAppBar(title = { Text("Book Explorer") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onFavoritesClick) {
                Icon(Icons.Default.Favorite, contentDescription = "Ulubione")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (val state = uiState) {
                is HomeUiState.Loading -> LoadingView()
                is HomeUiState.Error -> ErrorItem(state.message) { viewModel.loadBooks() }
                is HomeUiState.Success -> {
                    LazyColumn {
                        items(state.books) { book ->
                            val authorName = book.authors?.firstOrNull()?.name ?: "Nieznany autor"
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
