package com.xiaoyv.comic.reader.ui.screen.feature.bookinfo

import androidx.paging.LoadState
import com.xiaoyv.comic.reader.data.entity.BookSeriesEntity

/**
 * [BookInfoState]
 *
 * @author why
 * @since 5/1/24
 */
data class BookInfoState(
    val loadState: LoadState = LoadState.Loading,
    val bookSeriesEntity: BookSeriesEntity = BookSeriesEntity()
)
