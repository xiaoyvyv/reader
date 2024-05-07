package com.xiaoyv.comic.datasource.book.remote.impl.komga

import android.content.Context
import com.xiaoyv.comic.datasource.book.BookMetaData
import com.xiaoyv.comic.datasource.book.RemoteBookModel
import com.xiaoyv.comic.datasource.book.remote.impl.RemoteBookDetailEntity
import com.xiaoyv.comic.datasource.book.remote.impl.RemoteBookUserEntity
import com.xiaoyv.comic.datasource.book.remote.impl.RemoteLibrary
import com.xiaoyv.comic.datasource.book.remote.impl.RemoteLibraryConfig
import com.xiaoyv.comic.datasource.book.remote.impl.RemoteLibraryEntity
import com.xiaoyv.comic.datasource.book.remote.impl.RemoteSeriesInfo
import com.xiaoyv.comic.datasource.book.remote.impl.komga.entity.KomgaRemoteManifest
import com.xiaoyv.comic.datasource.book.remote.impl.komga.entity.KomgaRemoteSeriesBook
import com.xiaoyv.comic.datasource.book.remote.impl.komga.entity.KomgaRemoteSeriesBooks
import com.xiaoyv.comic.datasource.book.remote.impl.komga.entity.KomgaRemoteSeriesContent
import com.xiaoyv.comic.datasource.series.SeriesBook
import com.xiaoyv.comic.datasource.utils.runCatchingPrint
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

    override suspend fun getType(): Result<List<RemoteLibraryEntity>> {
        return withContext(Dispatchers.IO) {
            runCatchingPrint {
                manager.komga.getLibrary(authorization = config.basicAuthorization).map {
                    RemoteLibraryEntity(
                        config = config,
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

    override suspend fun getTypeSeries(
        typeId: String,
        page: Int,
        pageSize: Int
    ): Result<List<RemoteSeriesInfo>> {
        return withContext(Dispatchers.IO) {
            runCatchingPrint {
                manager.komga.getSeries(
                    authorization = config.basicAuthorization,
                    libraryId = typeId,
                    page = page,
                    size = pageSize
                ).content
                    .orEmpty()
                    .map { it.toSeriesInfo() }
            }
        }
    }

    override suspend fun getSeriesById(seriesId: String): Result<RemoteSeriesInfo> {
        return withContext(Dispatchers.IO) {
            runCatchingPrint {
                val result = awaitAll(
                    async {
                        manager.komga.getSeriesDetail(
                            authorization = config.basicAuthorization,
                            seriesId = seriesId
                        ).toSeriesInfo()
                    },
                    async {
                        manager.komga.getSeriesBooks(
                            authorization = config.basicAuthorization,
                            seriesId = seriesId
                        )
                    }
                )

                val seriesInfo = result[0] as RemoteSeriesInfo
                val seriesBooks = result[1] as KomgaRemoteSeriesBooks

                // 填充 books
                seriesInfo.books = seriesBooks.content.orEmpty().map {
                    SeriesBook(
                        cover = "${config.baseUrl}/api/v1/books/${it.id}/thumbnail",
                        title = it.name.orEmpty().ifBlank { it.seriesTitle.orEmpty() },
                        number = it.number,
                        book = RemoteBookModel(
                            config = config,
                            bookId = it.id.toString(),
                            seriesId = seriesId
                        ),
                        bookMeta = BookMetaData(
                            format = it.media?.mediaProfile.orEmpty(),
                            author = it.metadata?.authors
                                .orEmpty()
                                .joinToString(",") { author -> author?.name.orEmpty() },
                            title = it.metadata?.title.orEmpty(),
                            subject = it.metadata?.summary.orEmpty(),
                            keywords = it.metadata?.tags.orEmpty().joinToString(","),
                            creationDate = it.created?.toString().orEmpty(),
                            modDate = it.lastModified?.toString().orEmpty(),
                            creator = "",
                            producer = "",
                            encryption = "",
                        )
                    )
                }

                seriesInfo
            }
        }
    }

    override suspend fun getBookDetail(
        bookId: String,
        seriesId: String?
    ): Result<RemoteBookDetailEntity> {
        return withContext(Dispatchers.IO) {
            runCatchingPrint {
                val result = awaitAll(
                    async {
                        manager.komga.getBookDetail(
                            authorization = config.basicAuthorization,
                            bookId = bookId
                        )
                    },
                    async {
                        manager.komga.getBookManifest(
                            authorization = config.basicAuthorization,
                            bookId = bookId
                        )
                    }
                )

                val book = result[0] as KomgaRemoteSeriesBook
                val manifest = result[1] as KomgaRemoteManifest

                RemoteBookDetailEntity(
                    config = config,
                    id = book.id.orEmpty(),
                    typeId = book.libraryId.orEmpty(),
                    seriesId = book.seriesId.orEmpty(),
                    seriesTitle = book.seriesTitle.orEmpty(),
                    number = book.number,
                    created = book.created,
                    summary = book.metadata?.summary.orEmpty(),
                    publisher = "",
                    authors = "",
                    metaTitle = book.metadata?.title.orEmpty(),
                    lastModified = book.lastModified,
                    sizeBytes = book.sizeBytes,
                    format = book.media?.mediaProfile.orEmpty(),
                    pageCount = book.media?.pagesCount ?: 0,
                    status = book.media?.status.orEmpty(),
                    title = book.metadata?.title.orEmpty(),
                    cover = "${config.baseUrl}/api/v1/books/${book.id}/thumbnail",
                    pages = manifest.readingOrder
                        .orEmpty()
                        .mapIndexed { index, readingOrder ->
                            RemoteBookDetailEntity.Page(
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

    /*

        override suspend fun getBookDetail(seriesId: String): Result<RemoteSeriesEntity> {
            return withContext(Dispatchers.IO) {
                runCatchingPrint {
                    val deferred1: Deferred<RemoteSeriesEntity> = async {
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

                    val series = anies[0] as RemoteSeriesEntity
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
        }*/
    /*

        suspend fun getBookManifest(
            bookId: String,
            libraryId: String
        ): Result<RemoteBookManifestEntity> {

        }

    */

    private fun KomgaRemoteSeriesContent.toSeriesInfo(): RemoteSeriesInfo {
        return RemoteSeriesInfo(
            config = config,
            id = id.orEmpty(),
            typeId = libraryId.orEmpty(),
            booksCount = booksCount,
            cover = config.baseUrl + "/api/v1/series/${id}/thumbnail",
            created = created,
            tags = metadata?.tags.orEmpty()
                .ifEmpty { metadata?.genres.orEmpty() }
                .joinToString(","),
            status = metadata?.status.orEmpty(),
            authors = booksMetadata?.authors.orEmpty().joinToString(",") { it?.name.orEmpty() },
            title = metadata?.title.orEmpty(),
            summary = metadata?.summary.orEmpty(),
            publisher = metadata?.publisher.orEmpty(),
            language = metadata?.language.orEmpty(),
        )
    }
}