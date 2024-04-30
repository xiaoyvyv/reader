package com.xiaoyv.comic.reader.ui.screen.main.home.file

import androidx.paging.LoadState
import com.xiaoyv.comic.reader.data.entity.FileEntity

/**
 * [BookFileState]
 *
 * @author why
 * @since 4/26/24
 */
data class BookFileState(
    val loadState: LoadState = LoadState.Loading,
    val files: List<FileEntity> = emptyList(),
)
