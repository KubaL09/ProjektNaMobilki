package com.example.mylist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mylist.data.remote.BookDetailDto
import com.example.mylist.data.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface DetailUiState {
    object Loading : DetailUiState
    data class Success(val detail: BookDetailDto) : DetailUiState
    data class Error(val message: String) : DetailUiState
}

class BookDetailViewModel(private val repository: BookRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    fun loadBookDetails(workId: String) {
        _isFavorite.value = repository.isFavorite(workId)

        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading
            val result = repository.getBookDetails(workId)
            result.onSuccess { detail ->
                _uiState.value = DetailUiState.Success(detail)
            }.onFailure { e ->
                _uiState.value = DetailUiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun toggleFavorite(workId: String) {
        if (_isFavorite.value) {
            repository.removeFavorite(workId)
            _isFavorite.value = false
        } else {
            repository.addFavorite(workId)
            _isFavorite.value = true
        }
    }
}

class BookDetailViewModelFactory(private val repository: BookRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
