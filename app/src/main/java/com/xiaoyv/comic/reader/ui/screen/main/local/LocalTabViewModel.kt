package com.xiaoyv.comic.reader.ui.screen.main.local

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import com.xiaoyv.comic.reader.data.repository.file.BookFileRepository
import com.xiaoyv.comic.reader.data.repository.file.BookFileRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * [LocalTabViewModel]
 *
 * @author why
 * @since 5/4/24
 */
class LocalTabViewModel(
    application: Application,
    val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    private val repository: BookFileRepository = BookFileRepositoryImpl()

    private val _uiState = MutableStateFlow(LocalTabState())
    val uiState get() = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(loadState = LoadState.Loading) }

            val state = repository.scanBooks()
                .map {
                    LocalTabState(
                        loadState = LoadState.NotLoading(true),
                        books = it
                    )
                }
                .getOrElse {
                    LocalTabState(loadState = LoadState.Error(it))
                }

            _uiState.update { state }
        }
    }
}