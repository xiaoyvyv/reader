package com.xiaoyv.comic.datasource.impl.remote

import android.content.Context
import com.xiaoyv.comic.datasource.BookDataSource
import com.xiaoyv.comic.datasource.BookMetaData
import com.xiaoyv.comic.datasource.BookPage
import com.xiaoyv.comic.datasource.RemoteBookModel
import com.xiaoyv.comic.datasource.remote.RemoteBookSeriesEntity
import com.xiaoyv.comic.datasource.remote.RemoteBookManifestEntity
import com.xiaoyv.comic.datasource.remote.RemoteLibraryFactory
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

/**
 * [RemoteDataSource]
 *
 * @author why
 * @since 5/4/24
 */
class RemoteDataSource(
    override val context: Context,
    override val model: RemoteBookModel,
) : BookDataSource<RemoteBookModel> {
    private val remoteLibrary = RemoteLibraryFactory.create(context, model.config)
    private var bookDetail: RemoteBookSeriesEntity? = null
    private var bookManifest: RemoteBookManifestEntity? = null

    override var pageCount: Int = 0

    override fun load() {
        runBlocking {
            val results = awaitAll(
                async { remoteLibrary.getBookDetail(model.bookId, model.libraryId).getOrNull() },
                async { remoteLibrary.getBookManifest(model.bookId, model.libraryId).getOrNull() }
            )

            bookDetail = results[0] as? RemoteBookSeriesEntity
            pageCount = bookDetail?.bookCount ?: 0

            bookManifest = results[1] as? RemoteBookManifestEntity
        }
    }

    override fun getCover(): String {
        return bookDetail?.cover.orEmpty()
    }

    override fun getPage(page: Int): BookPage<RemoteBookModel, out BookDataSource<RemoteBookModel>> {
        return RemotePage(
            dataSource = this,
            page = page,
            manifestPage = bookManifest?.pages?.getOrNull(page)
        )
    }

    override fun getMetaInfo(): BookMetaData {
        val entity = requireNotNull(bookDetail)
        return BookMetaData(
            format = "REMOTE",
            encryption = "",
            author = entity.authors.joinToString(","),
            title = entity.title,
            subject = entity.summary,
            keywords = entity.tags.orEmpty().joinToString(","),
            creator = entity.publisher,
            producer = entity.publisher,
            creationDate = entity.created.toString(),
            modDate = entity.lastModified.toString()
        )
    }

    override fun supportExtension(): List<String> {
        return listOf("https", "http")
    }

    override fun destroy() {

    }
}