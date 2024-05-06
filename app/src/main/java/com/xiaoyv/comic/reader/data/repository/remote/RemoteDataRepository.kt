package com.xiaoyv.comic.reader.data.repository.remote

import androidx.paging.PagingSource
import com.xiaoyv.comic.datasource.book.remote.impl.RemoteLibraryConfig
import com.xiaoyv.comic.datasource.book.remote.impl.RemoteLibraryEntity
import com.xiaoyv.comic.datasource.book.remote.impl.RemoteSeriesInfo
import com.xiaoyv.comic.datasource.series.SeriesInfo

/**
 * [RemoteDataRepository]
 *
 * @author why
 * @since 5/1/24
 */
interface RemoteDataRepository {
    fun getPageSource(
        config: RemoteLibraryConfig,
        libraryId: String
    ): PagingSource<Int, RemoteSeriesInfo>

    suspend fun loadTabs(config: RemoteLibraryConfig): Result<List<RemoteLibraryEntity>>
}