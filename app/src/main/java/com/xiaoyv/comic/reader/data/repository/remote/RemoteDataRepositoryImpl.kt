package com.xiaoyv.comic.reader.data.repository.remote

import androidx.paging.PagingSource
import com.xiaoyv.comic.datasource.book.remote.impl.RemoteLibrary
import com.xiaoyv.comic.datasource.book.remote.impl.RemoteLibraryConfig
import com.xiaoyv.comic.datasource.book.remote.impl.RemoteLibraryEntity
import com.xiaoyv.comic.datasource.book.remote.impl.RemoteLibraryFactory
import com.xiaoyv.comic.datasource.book.remote.impl.RemoteSeriesInfo
import com.xiaoyv.comic.reader.application
import com.xiaoyv.comic.reader.data.defaultPagingSource
import java.util.concurrent.ConcurrentHashMap

/**
 * [RemoteDataRepositoryImpl]
 *
 * @author why
 * @since 5/1/24
 */
class RemoteDataRepositoryImpl : RemoteDataRepository {

    private val remoteLibraryCache = ConcurrentHashMap<Int, RemoteLibrary>()

    override fun getPageSource(
        config: RemoteLibraryConfig,
        libraryId: String
    ): PagingSource<Int, RemoteSeriesInfo> {
        return defaultPagingSource { current, size ->
            val remoteLibrary = remoteLibraryCache.getOrPut(config.type) {
                RemoteLibraryFactory.create(application, config)
            }

            val result = remoteLibrary.getTypeSeries(
                typeId = libraryId,
                page = current - 1,
                pageSize = size
            )

            result.getOrThrow()
        }
    }

    override suspend fun loadTabs(config: RemoteLibraryConfig): Result<List<RemoteLibraryEntity>> {
        val remoteLibrary = remoteLibraryCache.getOrPut(config.type) {
            RemoteLibraryFactory.create(application, config)
        }

        return remoteLibrary.getType()
    }
}