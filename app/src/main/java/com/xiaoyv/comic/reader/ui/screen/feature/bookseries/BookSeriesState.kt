package com.xiaoyv.comic.reader.ui.screen.feature.bookseries

import androidx.paging.LoadState
import com.xiaoyv.comic.datasource.book.remote.impl.RemoteSeriesInfo
import com.xiaoyv.comic.datasource.series.SeriesInfo

/**
 * [BookSeriesState]
 *
 * @author why
 * @since 5/1/24
 */
data class BookSeriesState(
    val loadState: LoadState = LoadState.Loading,
    val seriesInfo: RemoteSeriesInfo? = null
)