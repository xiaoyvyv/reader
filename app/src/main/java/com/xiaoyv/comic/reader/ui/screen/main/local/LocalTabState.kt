package com.xiaoyv.comic.reader.ui.screen.main.local

import androidx.paging.LoadState
import com.xiaoyv.comic.reader.data.entity.FileEntity

/**
 * [LocalTabState]
 *
 * @author why
 * @since 5/7/24
 */
data class LocalTabState(
    val loadState: LoadState = LoadState.Loading,
    val books: List<FileEntity> = emptyList()
)
