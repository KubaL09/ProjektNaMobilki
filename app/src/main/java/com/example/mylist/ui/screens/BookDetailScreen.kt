package com.example.mylist.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mylist.data.remote.getDescriptionString
import com.example.mylist.ui.components.ErrorItem
import com.example.mylist.ui.components.LoadingView
import com.example.mylist.ui.viewmodel.BookDetailViewModel
import com.example.mylist.ui.viewmodel.DetailUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    viewModel: BookDetailViewModel,
    workId: String,
    authorName: String,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()

    LaunchedEffect(workId) {
        viewModel.loadBookDetails(workId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Szczegóły książki") },
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
                is DetailUiState.Loading -> LoadingView()
                is DetailUiState.Error -> ErrorItem(state.message) { viewModel.loadBookDetails(workId) }
                is DetailUiState.Success -> {
                    val detail = state.detail
                    val coverId = detail.covers?.firstOrNull() ?: detail.covers?.firstOrNull()
                    
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Large Cover
                        if (coverId != null) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data("https://covers.openlibrary.org/b/id/$coverId-L.jpg")
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Okładka ${detail.title}",
                                modifier = Modifier
                                    .height(300.dp)
                                    .fillMaxWidth(),
                                contentScale = ContentScale.Fit
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .height(300.dp)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Brak okładki")
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = detail.title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        
                        Text(
                            text = "Autor: $authorName",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        if (detail.numberOfPages != null) {
                            Text(text = "Liczba stron: ${detail.numberOfPages}")
                        }
                        
                        if (detail.firstPublishDate != null) {
                            Text(text = "Rok publikacji: ${detail.firstPublishDate}")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { viewModel.toggleFavorite(workId) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = if (isFavorite) "Usuń z ulubionych" else "Dodaj do ulubionych")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Opis",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = detail.getDescriptionString(),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
