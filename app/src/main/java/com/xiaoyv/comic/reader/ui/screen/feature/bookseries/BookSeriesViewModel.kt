package com.xiaoyv.comic.reader.ui.screen.feature.bookseries

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import com.xiaoyv.comic.reader.data.repository.remote.RemoteSeriesRepository
import com.xiaoyv.comic.reader.data.repository.remote.RemoteSeriesRepositoryImpl
import com.xiaoyv.comic.reader.ui.utils.debugLog
import com.xiaoyv.comic.reader.ui.utils.mutableStateFlowOf
import com.xiaoyv.comic.reader.ui.utils.toJson
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * [BookSeriesViewModel]
 *
 * @author why
 * @since 4/29/24
 */
class BookSeriesViewModel(
    private val application: Application,
    savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {
    private val params = BookSeriesArguement(savedStateHandle)
    private val repository: RemoteSeriesRepository = RemoteSeriesRepositoryImpl()

    private val _uiState = mutableStateFlowOf<BookSeriesState>(BookSeriesState())
    internal val uiState get() = _uiState.asStateFlow()

    init {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            val state = repository.loadSeriesInfo(params.model)
                .map {
                    BookSeriesState(
                        loadState = LoadState.NotLoading(true),
                        seriesInfo = it
                    )
                }
                .getOrElse {
                    BookSeriesState(loadState = LoadState.Error(it))
                }


            _uiState.update { state }
        }
    }

    override fun onCleared() {

    }
}