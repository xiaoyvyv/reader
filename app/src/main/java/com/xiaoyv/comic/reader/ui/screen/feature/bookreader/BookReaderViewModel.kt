package com.xiaoyv.comic.reader.ui.screen.feature.bookreader

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import com.xiaoyv.comic.datasource.book.BookDataSource
import com.xiaoyv.comic.datasource.book.BookDataSourceFactory
import com.xiaoyv.comic.datasource.book.BookModel
import com.xiaoyv.comic.reader.data.repository.bookreader.BookReaderRepositoryImpl
import com.xiaoyv.comic.reader.ui.utils.mutableStateFlowOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * [BookReaderViewModel]
 *
 * @author why
 * @since 4/29/24
 */
class BookReaderViewModel(
    private val application: Application,
    savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {
    private val bookReaderRepository = BookReaderRepositoryImpl()

    private val params = ReaderViewArguement(savedStateHandle)

    private val _uiState = mutableStateFlowOf<BookReaderState>(BookReaderState())
    internal val uiState get() = _uiState.asStateFlow()

    private var bookDataSource: BookDataSource<BookModel>? = null

    init {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            bookDataSource = withContext(Dispatchers.IO) {
                BookDataSourceFactory.create(application, params.model)
                    .also { it.load() }
            }

            val state = bookReaderRepository.loadPages(bookDataSource)
                .map {
                    BookReaderState(
                        loadState = LoadState.NotLoading(true),
                        pages = it
                    )
                }
                .getOrElse {
                    BookReaderState(loadState = LoadState.Error(it))
                }

            _uiState.update { state }
        }
    }

    override fun onCleared() {
        runCatching {
            bookDataSource?.destroy()
            bookDataSource = null
        }
    }
}