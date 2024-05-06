package com.xiaoyv.comic.reader.ui.screen.feature.bookinfo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import com.xiaoyv.comic.reader.data.repository.bookinfo.BookInfoRepositoryImpl
import com.xiaoyv.comic.reader.ui.utils.mutableStateFlowOf
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * [BookInfoViewModel]
 *
 * @author why
 * @since 5/1/24
 */
class BookInfoViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {
    private val bookInfoRepository = BookInfoRepositoryImpl()
    private val bookInfoArguement = BookInfoArguement(savedStateHandle)

    private val _uiState = mutableStateFlowOf<BookInfoState>(BookInfoState())
    internal val uiState get() = _uiState.asStateFlow()

    init {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            val infoState = bookInfoRepository.loadBookInfo(bookInfoArguement.model)
                .map {
                    BookInfoState(
                        loadState = LoadState.NotLoading(true),
                        bookSeriesEntity = it
                    )
                }
                .getOrElse {
                    it.printStackTrace()

                    BookInfoState(loadState = LoadState.Error(it))
                }

            _uiState.update { infoState }
        }
    }
}