package com.xiaoyv.comic.reader.data.repository.remote

import androidx.paging.PagingSource
import com.xiaoyv.comic.datasource.remote.RemoteBookSeriesEntity
import com.xiaoyv.comic.datasource.remote.RemoteLibraryConfig
import com.xiaoyv.comic.datasource.remote.RemoteLibraryEntity

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
    ): PagingSource<Int, RemoteBookSeriesEntity>

    suspend fun loadTabs(config: RemoteLibraryConfig): Result<List<RemoteLibraryEntity>>
}