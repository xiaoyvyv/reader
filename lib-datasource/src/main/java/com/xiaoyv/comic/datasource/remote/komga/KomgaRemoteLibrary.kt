package com.xiaoyv.comic.datasource.remote.komga

import android.content.Context
import com.xiaoyv.comic.datasource.remote.RemoteBookDetailEntity
import com.xiaoyv.comic.datasource.remote.RemoteBookSeriesEntity
import com.xiaoyv.comic.datasource.remote.RemoteBookManifestEntity
import com.xiaoyv.comic.datasource.remote.RemoteBookUserEntity
import com.xiaoyv.comic.datasource.remote.RemoteLibrary
import com.xiaoyv.comic.datasource.remote.RemoteLibraryConfig
import com.xiaoyv.comic.datasource.remote.RemoteLibraryEntity
import com.xiaoyv.comic.datasource.remote.komga.entity.KomgaRemoteManifest
import com.xiaoyv.comic.datasource.remote.komga.entity.KomgaRemoteSeriesBooks
import com.xiaoyv.comic.datasource.remote.komga.entity.KomgaRemoteSeriesContent
import com.xiaoyv.comic.datasource.utils.runCatchingPrint
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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

    override suspend fun getLibraryBookSeries(
        libraryId: String,
        page: Int,
        pageSize: Int
    ): Result<List<RemoteBookSeriesEntity>> {
        return withContext(Dispatchers.IO) {
            runCatchingPrint {
                manager.komga.getSeries(
                    authorization = config.basicAuthorization,
                    libraryId = libraryId,
                    page = page,
                    size = pageSize
                ).content
                    .orEmpty()
                    .map { it.toBookSeriesEntity() }
            }
        }
    }

    override suspend fun getBookDetail(
        seriesId: String,
        libraryId: String
    ): Result<RemoteBookSeriesEntity> {
        return withContext(Dispatchers.IO) {
            runCatchingPrint {
                val deferred1: Deferred<RemoteBookSeriesEntity> = async {
                    manager.komga.getSeriesDetail(
                        authorization = config.basicAuthorization,
                        seriesId = seriesId
                    ).toBookSeriesEntity()
                }

                val deferred2: Deferred<KomgaRemoteSeriesBooks> = async {
                    manager.komga.getSeriesBooks(
                        authorization = config.basicAuthorization,
                        seriesId = seriesId
                    )
                }

                val anies = awaitAll(deferred1, deferred2)

                val series = anies[0] as RemoteBookSeriesEntity
                val books = anies[1] as KomgaRemoteSeriesBooks

                series.books = books.content.orEmpty().map {
                    RemoteBookDetailEntity(
                        config = config,
                        id = it.id.orEmpty(),
                        libraryId = it.libraryId.orEmpty(),
                        seriesId = it.seriesId.orEmpty(),
                        seriesTitle = it.seriesTitle.orEmpty(),
                        number = it.number,
                        sizeBytes = it.sizeBytes,
                        type = it.media?.mediaType.orEmpty(),
                        pageCount = it.media?.pagesCount ?: 0,
                        status = it.media?.status.orEmpty(),
                        title = it.metadata?.title.orEmpty(),
                        cover = "${config.baseUrl}/api/v1/books/${it.id}/thumbnail"
                    )
                }

                series
            }
        }
    }

    override suspend fun getBookManifest(
        bookId: String,
        libraryId: String
    ): Result<RemoteBookManifestEntity> {
        return withContext(Dispatchers.IO) {
            runCatchingPrint {
                val manifest = manager.komga.getBookManifest(
                    authorization = config.basicAuthorization,
                    bookId = bookId
                )

                RemoteBookManifestEntity(
                    pages = manifest.readingOrder
                        .orEmpty()
                        .mapIndexed { index, readingOrder: KomgaRemoteManifest.ReadingOrder ->
                            RemoteBookManifestEntity.Page(
                                number = index,
                                mime = readingOrder.type.orEmpty(),
                                width = readingOrder.width,
                                height = readingOrder.height,
                                url = readingOrder.href.orEmpty()
                            )
                        }
                )
            }
        }
    }


    private fun KomgaRemoteSeriesContent.toBookSeriesEntity(): RemoteBookSeriesEntity {
        return RemoteBookSeriesEntity(
            config = config,
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