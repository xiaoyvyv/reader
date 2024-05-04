package com.xiaoyv.comic.reader.data.repository.remote

import androidx.paging.PagingSource
import com.xiaoyv.comic.datasource.remote.RemoteBookEntity
import com.xiaoyv.comic.datasource.remote.RemoteLibrary
import com.xiaoyv.comic.datasource.remote.RemoteLibraryConfig
import com.xiaoyv.comic.datasource.remote.RemoteLibraryEntity
import com.xiaoyv.comic.datasource.remote.RemoteLibraryFactory
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
    ): PagingSource<Int, RemoteBookEntity> {
        return defaultPagingSource { current, size ->
            val remoteLibrary = remoteLibraryCache.getOrPut(config.type) {
                RemoteLibraryFactory.create(application, config)
            }

            val result = remoteLibrary.getLibraryBookList(
                libraryId = libraryId,
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

        return remoteLibrary.getLibraries()
    }
}