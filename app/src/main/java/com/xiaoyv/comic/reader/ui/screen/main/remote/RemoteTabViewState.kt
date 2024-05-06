package com.xiaoyv.comic.reader.ui.screen.main.remote

import androidx.paging.LoadState
import com.xiaoyv.comic.datasource.book.remote.impl.RemoteLibraryEntity

/**
 * [RemoteTabViewState]
 *
 * @author why
 * @since 5/4/24
 */
data class RemoteTabViewState(
    val loadState: LoadState = LoadState.Loading,
    val tabs: List<RemoteLibraryEntity> = emptyList(),
)
