package com.xiaoyv.comic.datasource.remote.komga

import android.content.Context
import android.util.Base64
import com.xiaoyv.comic.datasource.remote.RemoteBookEntity
import com.xiaoyv.comic.datasource.remote.RemoteBookUserEntity
import com.xiaoyv.comic.datasource.remote.RemoteLibrary
import com.xiaoyv.comic.datasource.remote.RemoteLibraryConfig
import com.xiaoyv.comic.datasource.remote.RemoteLibraryEntity
import com.xiaoyv.comic.datasource.remote.komga.entity.KomgaRemoteSeriesContent
import com.xiaoyv.comic.datasource.utils.runCatchingPrint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * [KomgaRemoteLibrary]
 *
 * @author why
 * @since 5/4/24
 */
class KomgaRemoteLibrary(
    override val context: Context,
    override val config: RemoteLibraryConfig
) : RemoteLibrary {

    private val manager by lazy {
        KomgaRemoteManager(context, config)
    }

    override suspend fun getLibraries(): Result<List<RemoteLibraryEntity>> {
        return withContext(Dispatchers.IO) {
            runCatchingPrint {
                manager.komga.getLibrary(authorization = config.basicAuthorization).map {
                    RemoteLibraryEntity(
                        id = it.id.orEmpty(),
                        title = it.name.orEmpty()
                    )
                }
            }
        }
    }

    override suspend fun login(): Result<RemoteBookUserEntity> {
        return withContext(Dispatchers.IO) {
            runCatchingPrint {
                val remoteUser = manager.komga.getMeInfo(authorization = config.basicAuthorization)

                RemoteBookUserEntity(
                    id = remoteUser.id.orEmpty(),
                    username = remoteUser.email.orEmpty(),
                    roles = remoteUser.roles.orEmpty()
                )
            }
        }
    }

    override suspend fun getLibraryBookList(
        libraryId: String,
        page: Int,
        pageSize: Int
    ): Result<List<RemoteBookEntity>> {
        return withContext(Dispatchers.IO) {
            runCatchingPrint {
                manager.komga.getSeries(
                    authorization = config.basicAuthorization,
                    libraryId = libraryId,
                    page = page,
                    size = pageSize
                ).content
                    .orEmpty()
                    .map { it.toBookEntity() }
            }
        }
    }

    override suspend fun getBookDetail(
        bookId: String,
        libraryId: String
    ): Result<RemoteBookEntity> {
        return withContext(Dispatchers.IO) {
            runCatchingPrint {
                manager.komga.getBookDetail(
                    authorization = config.basicAuthorization,
                    bookId = bookId
                ).toBookEntity()
            }
        }
    }

    private fun KomgaRemoteSeriesContent.toBookEntity(): RemoteBookEntity {
        return RemoteBookEntity(
            id = id.orEmpty(),
            libraryId = libraryId.orEmpty(),
            bookCount = booksCount,
            name = name.orEmpty(),
            cover = config.baseUrl + "/api/v1/series/${id}/thumbnail",
            lastModified = lastModified,
            created = created,
            tags = metadata?.tags.orEmpty(),
            alternateTitles = metadata?.alternateTitles.orEmpty()
                .map { title -> title?.title + ": " + title?.label },
            status = metadata?.status.orEmpty(),
            authors = booksMetadata?.authors.orEmpty()
                .map { author -> author?.role + ": " + author?.name },
            title = metadata?.title.orEmpty(),
            summary = metadata?.summary.orEmpty(),
            publisher = metadata?.publisher.orEmpty(),
            language = metadata?.language.orEmpty(),
            genres = metadata?.genres.orEmpty()
        )
    }
}