package com.example.mylist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mylist.data.remote.BookWorkDto
import com.example.mylist.data.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(val books: List<BookWorkDto>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

class HomeViewModel(private val repository: BookRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadBooks()
    }

    fun loadBooks() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            val result = repository.getFictionBooks()
            result.onSuccess { books ->
                _uiState.value = HomeUiState.Success(books)
            }.onFailure { e ->
                _uiState.value = HomeUiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}

class HomeViewModelFactory(private val repository: BookRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
