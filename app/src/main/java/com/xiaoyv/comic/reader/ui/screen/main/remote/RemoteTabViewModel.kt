package com.xiaoyv.comic.reader.ui.screen.main.remote

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import com.xiaoyv.comic.datasource.remote.RemoteLibraryConfig
import com.xiaoyv.comic.datasource.remote.RemoteLibraryType
import com.xiaoyv.comic.reader.data.repository.remote.RemoteDataRepository
import com.xiaoyv.comic.reader.data.repository.remote.RemoteDataRepositoryImpl
import com.xiaoyv.comic.reader.ui.utils.debugLog
import com.xiaoyv.comic.reader.ui.utils.toJson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * [RemoteTabViewModel]
 *
 * @author why
 * @since 5/4/24
 */
class RemoteTabViewModel(
    application: Application,
    val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    private val remoteDataRepository: RemoteDataRepository = RemoteDataRepositoryImpl()

    private val _uiState = MutableStateFlow(RemoteTabViewState())
    val uiState get() = _uiState.asStateFlow()

    init {
        refresh()
    }

    private fun refresh() {
        val config = RemoteLibraryConfig(
            type = RemoteLibraryType.TYPE_KOMGA,
            username = "2333@qq.com",
            password = "2333@qq.com",
            baseUrl = "https://manga.pilipiliultra.com"
        )

        viewModelScope.launch {
            val result = remoteDataRepository.loadTabs(config)
                .map {
                    RemoteTabViewState(
                        loadState = LoadState.NotLoading(true),
                        tabs = it
                    )
                }
                .getOrElse {
                    RemoteTabViewState(loadState = LoadState.Error(it))
                }

            debugLog { result.toJson(true) }

            _uiState.update { result }
        }



    }
}