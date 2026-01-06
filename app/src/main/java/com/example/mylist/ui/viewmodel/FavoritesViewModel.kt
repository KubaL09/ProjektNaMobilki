package com.example.mylist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mylist.data.remote.SearchResultDto
import com.example.mylist.data.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface FavoritesUiState {
    object Loading : FavoritesUiState
    data class Success(val favorites: List<SearchResultDto>) : FavoritesUiState
    object Empty : FavoritesUiState
    data class Error(val message: String) : FavoritesUiState
}

class FavoritesViewModel(private val repository: BookRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<FavoritesUiState>(FavoritesUiState.Loading)
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    fun loadFavorites() {
        viewModelScope.launch {
            _uiState.value = FavoritesUiState.Loading
            val result = repository.getFavoriteBooks()
            result.onSuccess { list ->
                if (list.isEmpty()) {
                    _uiState.value = FavoritesUiState.Empty
                } else {
                    _uiState.value = FavoritesUiState.Success(list)
                }
            }.onFailure { e ->
                _uiState.value = FavoritesUiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}

class FavoritesViewModelFactory(private val repository: BookRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoritesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
